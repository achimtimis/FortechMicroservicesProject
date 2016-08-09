package com.cartservice.controller;

import com.cartservice.messaging.CartProcessor;
import com.cartservice.repository.ShoppingCartProductsRepository;
import com.cartservice.repository.ShoppingCartRepository;
import com.shopcommon.model.*;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Achim Timis on 7/7/2016.
 */
@RestController
@RequestMapping("/carts")
public class ShoppingCartController {
    Logger logger;

    @Autowired
    ShoppingCartProductsRepository shoppingCartProductsRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    @Qualifier(CartProcessor.OUTPUT_UPDATE)
    private MessageChannel channel_update;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private LoadBalancerClient loadBalancer;

    /**
     * USED TO MAKE REST CALLS
     **/
    RestTemplate restTemplate;

    public ShoppingCartController() {
        logger = Logger.getLogger(ShoppingCartController.class);
        restTemplate = new RestTemplate();
    }

    /**
     * Return all carts
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ShoppingCart[] getShoppingCarts() {
        ShoppingCart[] carts = (ShoppingCart[]) this.shoppingCartRepository.findAll().toArray();
        logger.info("A number of " + carts.length + " were sent via Rest API");
        return carts;
    }

    /**
     * Returns a specific cart
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ShoppingCart getShoppingCart(@PathVariable Long id) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findOne(id);
        if (shoppingCart != null) {
            logger.info("Sent via Rest API: " + shoppingCart);
            return shoppingCart;
        } else {
            logger.info("No shopping cart was found with id " + id);
        }
        return null;
    }

    /**
     * Delete a specific shopping cart
     *
     * @param id
     */
    @StreamListener(CartProcessor.INPUT_DELETE)
    public void deleteShoppingCart(Long id) {
        logger.info("Shopping cart with id " + id + " was deleted");
        shoppingCartRepository.delete(id);
    }

    /**
     * Create a shopping cart for a specific user
     *
     * @param userId
     * @return
     */
    @StreamListener(CartProcessor.INPUT_CREATE)
    public void create(Long userId) {
        User user = this.receiveUser(userId);
        if (user != null) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserid(user.getId());
            logger.info("Shopping cart created for user id " + user.getId());
            shoppingCartRepository.saveAndFlush(shoppingCart);
        }
        logger.warn("Shopping cart was not created for user id " + user.getId() + ", user not found");
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ShoppingCart findShoppingCartByUserId(@PathVariable Long id) {
        ShoppingCart cart = this.shoppingCartRepository.findByUserid(id);
        logger.info("Sent via Rest API cart: " + cart);
        return cart;
    }

    /**
     * Get user from a specific cart
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCartUser(@PathVariable("id") Long id) {
        ShoppingCart cart = shoppingCartRepository.findOne(id);
        User user = receiveUser(cart.getUserid());
        if (user != null) {
            logger.info("Requested via Rest API user: " + user);
        } else {
            logger.info("No user found!");
        }
        return null;
    }

    /**
     * Get products details from a specific cart
     *
     * @param cartId
     * @return
     */
    @RequestMapping(value = "/{id}/products", method = RequestMethod.GET)
    public List<Product> getCartProducts(@PathVariable("id") Long cartId) {
        List<Product> products = new ArrayList<>();
        logger.info("Requested from product-service products from cart with id " + cartId);
        ShoppingCart cart = shoppingCartRepository.findOne(cartId);
        cart.getProductsList().forEach(cartProduct -> products.add(this.receiveProduct(cartProduct.getProductId())));
        return products;
    }

    /**
     * Add a specific product with a specific quantity to a cart
     *
     * @param info Map with info about cart id, product id and product quantity
     * @return
     */
    @StreamListener(CartProcessor.INPUT_ADD_PRODUCT)
    public void addProductToShoppingCart(Map<String, Long> info) {
        Long id = info.get("id");
        Long productId = info.get("productId");
        Integer quantity = Integer.parseInt(info.get("quantity").toString());

        Product product = this.receiveProduct(productId);

        if (product.getStock() >= quantity && product != null) {
            ShoppingCart shoppingCart = shoppingCartRepository.findOne(shoppingCartRepository.findByUserid(id).getId());
            ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct(product.getId(), quantity, product.getName(), product.getPrice());
            shoppingCartProduct.setShoppingCart(shoppingCart);
            shoppingCart.getProductsList().add(shoppingCartProduct);

            product.setStock(product.getStock() - quantity);

            this.sendUpdateProduct(product);

            shoppingCartProductsRepository.save(shoppingCartProduct);
            shoppingCartRepository.save(shoppingCart);

            logger.info("Product " + product + " was added to shopping cart with id " + id);
        } else if (product == null) {
            logger.warn("No product found with id " + productId);
        } else if (product.getStock() < quantity) {
            logger.warn("Not enough products on stock");
        }
    }

    /**
     * Delete a specific product from a specific cart
     *
     * @param info Map with info about cart id and product id
     * @return
     */
    @StreamListener(CartProcessor.INPUT_REMOVE_PRODUCT)
    public void removeProductFromShoppingCart(Map<String, Long> info) {
        Long id = info.get("id");
        Long productId = info.get("productId");

        Product product = this.receiveProduct(productId);
        logger.info("Deleted from shopping cart product with id:" + productId);
        if (product != null) {
            ShoppingCart shoppingCart = this.shoppingCartRepository.findOne(id);
            Optional<ShoppingCartProduct> ocp = shoppingCart.getProductsList().stream().filter(p -> p.getProductId().equals(productId)).findFirst();
            if (ocp.isPresent()) {
                product.setStock(product.getStock() + ocp.get().getQuantity());
                this.sendUpdateProduct(product);

                shoppingCart.setProductsList(shoppingCart.getProductsList().stream().filter(p -> !p.getProductId().equals(productId)).collect(Collectors.toList()));

                shoppingCartProductsRepository.delete(ocp.get().getId());

                logger.info("Deleted product " + product + " from shopping cart with id " + id);

                shoppingCartRepository.saveAndFlush(shoppingCart);
            } else {
                logger.warn("Product not found in shopping cart");
            }
        }
        logger.warn("Product not found in database");
    }

    /**
     * Convert the shopping cart to an order and remove the shopping cart
     *
     * @param id
     */
    @StreamListener(CartProcessor.INPUT_CONFIRM_ORDER)
    public void confirmOrder(Long id) {
        ShoppingCart cart = shoppingCartRepository.findOne(id);

        Order order = new Order();
        order.setDate(LocalDateTime.now());
        order.setUserId(cart.getUserid());

        List<OrderProduct> orderProducts = new ArrayList<>();
        cart.getProductsList().forEach(shoppingCartProduct -> {
            OrderProduct op = new OrderProduct();
            op.setProductId(shoppingCartProduct.getProductId());
            op.setProductName(shoppingCartProduct.getProductName());
            op.setProductPrice(shoppingCartProduct.getProductPrice());
            op.setQuantity(shoppingCartProduct.getQuantity());
            orderProducts.add(op);
        });
        order.setProducts(orderProducts);
        sendOrder(order);
        shoppingCartRepository.delete(id);
    }

    /**
     * Get a product from product-service
     *
     * @param productId
     * @return
     */
    private Product receiveProduct(Long productId) {
        ServiceInstance instance = loadBalancer.choose("product-service");
        return restTemplate.getForObject(instance.getUri() + "/products/" + productId, Product.class);
    }

    /**
     * Send and updated product to the product service
     *
     * @param product
     */
    private void sendUpdateProduct(Product product) {
        logger.info("SENT VIA OUTPUT_PRODUCT_UPDATE: " + product);
        channel_update.send(MessageBuilder.withPayload(product).build());
    }

    /**
     * Send a order to order service
     *
     * @param order
     */
    private void sendOrder(Order order) {
        URL url = null;

        rabbitTemplate.convertAndSend("order-queue", order);
        logger.info("Sent to order-service order:" + order);

        try {
            url = new URL("http://localhost:9999/orders");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.getResponseCode();

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (ProtocolException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Receive a user from user-service
     *
     * @param userId
     * @return
     */
    private User receiveUser(Long userId) {
        User user = null;

        try {
            URL url = new URL("http://localhost:9999/users/" + userId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.getResponseCode();

            user = (User) rabbitTemplate.receiveAndConvert("user-queue");
            logger.info("Received from user-service user: " + user);

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (ProtocolException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return user;
    }

    /**************************************/
    /**********PRODUCT RECEIVERS***********/
    /**************************************/

    /**
     * Receiver for all products
     *
     * @return
     */
    public List<Product> getAllProducts() {
        ServiceInstance instance = loadBalancer.choose("product-service");
        List<Product> products = Arrays.asList(restTemplate.getForObject(instance.getUri() + "/products", Product[].class));
        logger.info("Received from product service: " + products);
        return products;
    }

    /**
     * Receiver for product
     *
     * @param id
     * @return
     */
    public Product getProduct(Long id) {
        ServiceInstance instance = loadBalancer.choose("product-service");
        Product product = restTemplate.getForObject(instance.getUri() + "/products/" + id, Product.class);
        logger.info("Received from product service: " + product);
        return product;
    }
}
