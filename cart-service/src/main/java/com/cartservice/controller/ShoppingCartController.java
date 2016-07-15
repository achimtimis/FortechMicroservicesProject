package com.cartservice.controller;

import com.cartservice.repository.ShoppingCartProductsRepository;
import com.cartservice.repository.ShoppingCartRepository;
import com.shopcommon.model.*;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Achim Timis on 7/7/2016.
 */
@RestController
@RequestMapping("/carts")
public class ShoppingCartController {

    Logger logger = Logger.getLogger(ShoppingCartController.class);

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private AmqpAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ShoppingCartProductsRepository shoppingCartProductsRepository;

    /**
     * Send all shopping carts from the database via cart-queue
     *
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    public List<ShoppingCart> getShoppingCarts(){
        List<ShoppingCart> carts = this.shoppingCartRepository.findAll();

        rabbitTemplate.convertAndSend("cart-queue", carts);

        logger.info("A number of " + carts.size() + " were sent via cart-queue");

        return carts;

    }

    /**
     * Send a specific shopping cart via cart-queue
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method=RequestMethod.GET)
    public ShoppingCart getShoppingCart(@PathVariable  Long id ) {
        ShoppingCart shoppingCart= this.shoppingCartRepository.findOne(id);

        rabbitAdmin.purgeQueue("cart-queue", false);

        if(shoppingCart != null){
            rabbitTemplate.convertAndSend("cart-queue", shoppingCart);

            logger.info("Sent via cart-queue: " + shoppingCart);
        }
        else{
            logger.info("No shopping cart was found with id " + id);
        }
        return shoppingCart;
    }

    /**
     * Delete a specific shopping cart
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteShoppingCart(@PathVariable Long id){
        logger.info("Shopping cart with id " + id + " was deleted from the database");

        shoppingCartRepository.delete(id);
    }


    @RequestMapping(value = "/user/{userId}", method = RequestMethod.POST)
    public ShoppingCart create(@PathVariable("userId") Long userId){
        User user = this.receiveUser(userId);

        if(user != null){
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserid(user.getId());

            logger.info("Shopping cart created for user id " + user.getId());

            return shoppingCartRepository.saveAndFlush(shoppingCart);
        }

        logger.info("Shopping cart was not created for user id " + user.getId() + ", user not found");

        return null;
}

    @RequestMapping(value = "/user/{id}",method=RequestMethod.GET)
    public ShoppingCart findShoppingCartByUserId(@PathVariable Long id){

        rabbitAdmin.purgeQueue("cart-queue", false);

        ShoppingCart cart = this.shoppingCartRepository.findByUserid(id);
        logger.info("Sent over cart-queue cart: " + cart);
        rabbitTemplate.convertAndSend("cart-queue",cart);

        return cart;
    }

    /**
     * Get user from a specific cart
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    public User getCartUser(@PathVariable("id") Long id){
        ShoppingCart cart = shoppingCartRepository.findOne(id);

        rabbitAdmin.purgeQueue("user-queue", false);

        URL obj = null;
        try {
            obj = new URL("http://localhost:9999/user-service/users/" + cart.getUserid());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.getResponseCode();

            User user = (User)rabbitTemplate.receiveAndConvert("user-queue");

            logger.info("Received from user-service: " +  user);

            return user;

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("No user found!");
        return null;
    }

    /**
     * Get products details from a specific cart
     *
     * @param cartId
     * @return
     */
    @RequestMapping(value = "/{id}/products", method = RequestMethod.GET)
    public List<Product> getCartProducts(@PathVariable("id") Long cartId){


        rabbitAdmin.purgeQueue("cart-queue", false);

        rabbitAdmin.purgeQueue("product-queue", false);

        List<Product> products = new ArrayList<>();

        logger.info("Requested from product-service products from cart with id " +  cartId);

        ShoppingCart cart = shoppingCartRepository.findOne(cartId);

        cart.getProductsList().forEach(cartProduct -> products.add(this.receiveProduct(cartProduct.getProductId())));

        logger.info("Received from product-service a number of " +  products.size() + " products");
        return products;
    }

    /**
     * Add a product to the shopping cart
     *
     * @param id
     * @param productId
     * @param quantity
     * @return
     */
    @RequestMapping(value = "/{id}/products", method = RequestMethod.POST)
    public ShoppingCart addProductToShoppingCart(@PathVariable("id") Long id, @RequestParam(value = "productId") Long productId,@RequestParam(value = "quantity") int quantity){

        Product product = this.receiveProduct(productId);

        if (product.getStock() >= quantity && product != null){
            ShoppingCart shoppingCart=this.shoppingCartRepository.findOne(id);
            ShoppingCartProduct shoppingCartProduct= new ShoppingCartProduct(product.getId(),quantity,product.getName(),product.getPrice());
            shoppingCartProduct.setShoppingCart(shoppingCart);
            shoppingCart.getProductsList().add(shoppingCartProduct);

            product.setStock(product.getStock() -  quantity);

            this.sendUpdateProduct(product);

            shoppingCartProductsRepository.save(shoppingCartProduct);
            shoppingCartRepository.save(shoppingCart);

            logger.info("Product " + product + " was added to shopping cart with id " + id);

            return shoppingCart;
        }

        else if(product == null){
            logger.warn("No product found with id " + productId);
        }

        else if(product.getStock() < quantity) {
            logger.warn("Not enough products on stock");
        }
        return null;
    }

    /**
     * Delete a product from a specific shopping cart
     *
     * @param id
     * @param productId
     * @return
     */
    @RequestMapping(value = "/{id}/products", method = RequestMethod.DELETE)
    public ShoppingCart removeProductFromShoppingCart(@PathVariable("id") Long id, @RequestParam(value = "productId") Long productId){
        Product product = this.receiveProduct(productId);

        logger.info("Deleted from shopping cart product with id:" + productId);

        if(product != null){
            ShoppingCart shoppingCart = this.shoppingCartRepository.findOne(id);

            Optional<ShoppingCartProduct> ocp = shoppingCart.getProductsList().stream().filter(p -> p.getProductId().equals(productId)).findFirst();
            if(ocp.isPresent()){
                product.setStock(product.getStock() + ocp.get().getQuantity());
                this.sendUpdateProduct(product);


                shoppingCart.setProductsList(shoppingCart.getProductsList().stream().filter(p -> !p.getProductId().equals(productId)).collect(Collectors.toList()));

                shoppingCartProductsRepository.delete(ocp.get().getId());

                logger.info("Deleted product " + product + " from shopping cart with id " + id);

                return this.shoppingCartRepository.saveAndFlush(shoppingCart);
            }
            else{
                logger.warn("Product not found in shopping cart");
                return null;
            }
        }
        logger.warn("Product not found in database");
        return null;
    }

    /**
     * Convert the shopping cart to an order and remove the shopping cart
     *
     * @param id
     */
    @RequestMapping(value = "/{id}/order", method = RequestMethod.POST)
    public void confirmOrder(@PathVariable("id") Long id){
        ShoppingCart cart = shoppingCartRepository.findOne(id);

        Order order = new Order();
        order.setDate(LocalDateTime.now());
        order.setUserId(cart.getUserid());

        List<OrderProduct> orderProducts = new ArrayList<>();
        cart.getProductsList().stream().forEach(shoppingCartProduct -> {
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
    private Product receiveProduct(Long productId){
        URL url = null;
        Product product = null;

        try {
            url = new URL("http://localhost:9999/product-service/products/" + productId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.getResponseCode();

            product = (Product)rabbitTemplate.receiveAndConvert("product-queue");
            logger.info("Received from product-service product: " + product);

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (ProtocolException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return product;
    }

    /**
     * Send and updated product to the product service
     *
     * @param product
     */
    private void sendUpdateProduct(Product product){
        URL url = null;

        rabbitTemplate.convertAndSend("product-queue", product);
        logger.info("Sent to product-service product " +  product);

        try {
            url = new URL("http://localhost:9999/product-service/products");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
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
     * Send a order to order service
     *
     * @param order
     */
    private void sendOrder(Order order){
        URL url = null;

        rabbitTemplate.convertAndSend("order-queue", order);
        logger.info("Sent to order-service order:" +  order);

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
    private User receiveUser(Long userId){
        User user = null;

        try {
            URL url= new URL("http://localhost:9999/users/" + userId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.getResponseCode();

            user = (User)rabbitTemplate.receiveAndConvert("user-queue");
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

}
