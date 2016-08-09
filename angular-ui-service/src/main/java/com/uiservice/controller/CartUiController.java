package com.uiservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shopcommon.model.Order;
import com.shopcommon.model.ShoppingCart;
import com.uiservice.messaging.CartProcessor;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by Flaviu Cicio on 13.07.2016.
 */
@RestController
@RequestMapping(value = "/api/carts")
public class CartUiController {
    Logger logger;

    /*******
     * CHANNELS
     *******/
    @Autowired
    @Qualifier(CartProcessor.OUTPUT_REMOVE_PRODUCT)
    private MessageChannel remove_product;
    @Autowired
    @Qualifier(CartProcessor.OUTPUT_ADD_PRODUCT)
    private MessageChannel add_product;
    @Autowired
    @Qualifier(CartProcessor.OUTPUT_CONFIRM_ORDER)
    private MessageChannel confirm_order;
    @Autowired
    @Qualifier(CartProcessor.OUTPUT_CREATE)
    private MessageChannel create_cart;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private LoadBalancerClient loadBalancer;

    RestTemplate restTemplate;

    public CartUiController() {
        logger = Logger.getLogger(CartUiController.class);
        restTemplate = new RestTemplate();
    }

    /**
     * Return all shopping carts
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "getAllCartsFallback")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ShoppingCart> getAllCarts() {
        ServiceInstance instance = loadBalancer.choose("cart-service");
        return Arrays.asList(restTemplate.getForObject(instance.getUri() + "/carts", ShoppingCart[].class));
    }

    /**
     * Return shopping cart with a given id
     *
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "getCartFallback")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ShoppingCart getCart(@PathVariable("id") Long id) {
        ShoppingCart cart = receiveShoppingCartById(id);
        return cart;
    }

    /**
     * Return cart with a given user id
     *
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "getCartFallback")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ShoppingCart getShoppingCartByUserId(@PathVariable("id") Long id) {
        ShoppingCart cart = receiveShoppingCartByUserId(id);
        return cart;
    }

    /**
     * Remove a product with a given id from a cart with a given id
     *
     * @param cid cart id
     * @param pid product id
     * @return
     */
    @HystrixCommand(fallbackMethod = "removeProductFallback")
    @RequestMapping(value = "/{cid}/products/{pid}", method = RequestMethod.GET)
    public void removeProduct(@PathVariable Long cid, @PathVariable Long pid) {
        Map<String, Long> info = new HashMap<>();
        info.put("id", cid);
        info.put("productId", pid);

        logger.info("SENT VIA REMOVE_PRODUCT: " + info);
        remove_product.send(MessageBuilder.withPayload(info).build());
    }

    /**
     * Add a given product to a given shopping cart
     *
     * @param userId
     * @param productId
     * @param quantity
     * @return
     */
    @HystrixCommand(fallbackMethod = "addProductToShoppingCartFallback")
    @RequestMapping(value = "user/{userId}/products", method = RequestMethod.GET)
    public void addProductToShoppingCart(@PathVariable("userId") Long userId, @RequestParam(value = "productId") Long productId, @RequestParam(value = "quantity") int quantity) {
        Map<String, Long> info = new HashMap<>();
        info.put("id", userId);
        info.put("productId", productId);
        info.put("quantity", Long.parseLong(String.valueOf(quantity)));

        add_product.send(MessageBuilder.withPayload(info).build());

        logger.info("SENT VIA ADD_PRODUCT: " + info);
    }

    /**
     * Check out all the items of a shopping cart with a given id
     *
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "checkoutFallback")
    @RequestMapping(value = "/{id}/order", method = RequestMethod.GET)
    public void checkout(@PathVariable("id") Long id) {
        confirm_order.send(MessageBuilder.withPayload(id).build());
        logger.info("Order with id " + id + " confirmed");
    }

    /**
     * Create a new cart
     *
     * @param userId
     * @return
     */
    @HystrixCommand(fallbackMethod = "newShoppingCartFallback")
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public void newShoppingCart(@Validated @RequestParam("userId") Long userId) {
        create_cart.send(MessageBuilder.withPayload(userId).build());
        logger.info("New cart was created for user with id " + userId);
    }


    /*******************
     *****RECEIVERS*****
     *******************/

    /**
     * Receiver for shopping cart by id
     *
     * @param id
     * @return
     */
    private ShoppingCart receiveShoppingCartById(Long id) {
        ServiceInstance instance = loadBalancer.choose("cart-service");
        ShoppingCart cart = restTemplate.getForObject(instance.getUri() + "/carts/" + id, ShoppingCart.class);

        if (cart != null) {
            logger.info("Cart " + cart + " was received from Rest API");
        } else {
            logger.info("No cart was found with id " + id);
        }
        return cart;
    }

    /**
     * Receiver for shopping cart by user id
     *
     * @param id
     * @return
     */
    private ShoppingCart receiveShoppingCartByUserId(Long id) {
        ServiceInstance instance = loadBalancer.choose("cart-service");
        ShoppingCart cart = restTemplate.getForObject(instance.getUri() + "/carts/user/" + id, ShoppingCart.class);

        if (cart != null) {
            logger.info("Cart " + cart + " was received from Rest API");
        } else {
            logger.info("No cart was found for user with id " + id);
        }
        return cart;
    }


    /**************************
     **** FALLBACK METHODS*****
     *************************/

    /**
     * Fallback method for getAllCarts
     *
     * @return
     */
    List<ShoppingCart> getAllCartsFallback() {
        logger.warn("getAllCartsFallback");
        return new ArrayList<>();
    }

    /**
     * Fallback method for getCart
     *
     * @param id
     * @return
     */
    ShoppingCart getCartFallback(Long id) {
        logger.warn("getCartFallback");
        return null;
    }

    /**
     * Fallback method for removeProduct
     *
     * @param cartId
     * @param productId
     * @return
     */
    void removeProductFallback(Long cartId, Long productId) {
        logger.warn("removeProductFallback");
    }

    /**
     * Fallback method for addProductToShoppingCart
     *
     * @param userId
     * @param productId
     * @param quantity
     */
    void addProductToShoppingCartFallback(Long userId, Long productId, int quantity) {
        logger.warn("addProductToShoppingCartFallback");
    }

    /**
     * Fallback method for checkout
     *
     * @param id
     */
    void checkoutFallback(Long id) {
        logger.warn("checkoutFallback");
    }

    /**
     * Fallback method for newShoppingCart
     *
     * @param userId
     */
    private void newShoppingCartFallback(Long userId) {
        logger.warn("newShoppingCartFallback");
    }
}
