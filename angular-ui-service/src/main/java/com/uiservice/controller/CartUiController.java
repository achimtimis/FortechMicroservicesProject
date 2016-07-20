package com.uiservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shopcommon.model.Order;
import com.shopcommon.model.ShoppingCart;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flaviu Cicio on 13.07.2016.
 */
@RestController
@RequestMapping(value = "/api/carts")
public class CartUiController {

    Logger logger = Logger.getLogger(ProductUiController.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public List<ShoppingCart> getAllShoppingCarts(){

        List<ShoppingCart> carts = new ArrayList<>();

        try {
            URL obj = new URL("http://localhost:9999/carts");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.getResponseCode();

            carts.addAll((List<ShoppingCart>) rabbitTemplate.receiveAndConvert("cart-queue"));

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return carts;
    }

    private ShoppingCart getCartFallback(Long id){
        logger.info("getCartFallback");return null;
    }

    @HystrixCommand(fallbackMethod = "getCartFallback" )
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ShoppingCart getShoppingCartById(@PathVariable("id") Long id){
        ShoppingCart cart = receiveShoppingCartById(id);

        return cart;
    }
    @HystrixCommand(fallbackMethod = "getCartFallback" )
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ShoppingCart getShoppingCartByUserId(@PathVariable("id") Long id){
        ShoppingCart cart = receiveShoppingCartByUserId(id);

        return cart;
    }
    private ShoppingCart removeProductFallback(Long cid,Long pid){
        logger.info("removeProductFallback");
        return null;
    }

    @HystrixCommand(fallbackMethod = "removeProductFallback" )
    @RequestMapping(value = "/{cid}/products/{pid}",method= RequestMethod.GET)
    public ShoppingCart removeProduct(@PathVariable Long cid,@PathVariable Long pid){
        ShoppingCart shoppingCart = receiveShoppingCartById(cid);
        logger.info("cart-controller -> remove product");
        try {
            URL obj = new URL("http://localhost:9999/carts/" + cid + "/products?" + "productId=" + pid);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("DELETE");
            con.getResponseCode();

            URL obj2 = new URL("http://localhost:9999/carts/" + cid);
            HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();
            con2.setRequestMethod("GET");
            con2.getResponseCode();

            shoppingCart = (ShoppingCart)rabbitTemplate.receiveAndConvert("cart-queue");

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return shoppingCart;
    }

    private ShoppingCart addProductToShoppingCartFallback( Long userId,Long productId,int quantity){logger.info("addProductToShoppingCartFallback");return null;}
    @HystrixCommand(fallbackMethod = "addProductToShoppingCartFallback" )
    @RequestMapping(value = "user/{userId}/products", method = RequestMethod.GET)
    public ShoppingCart addProductToShoppingCart(@PathVariable("userId") Long userId, @RequestParam(value = "productId") Long productId,@RequestParam(value = "quantity") int quantity){
        ShoppingCart shoppingCart = null;


        try {
            URL obj2 = new URL("http://localhost:9999/carts/user/" + userId);
            HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();
            con2.setRequestMethod("GET");
            con2.getResponseCode();

            shoppingCart = (ShoppingCart)rabbitTemplate.receiveAndConvert("cart-queue");

            logger.info("Received from cart-queue cart:" + shoppingCart);

            URL obj = new URL("http://localhost:9999/carts/" + shoppingCart.getId() + "/products?" + "productId=" + productId + "&quantity=" + quantity );
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.getResponseCode();

            URL obj3 = new URL("http://localhost:9999/carts/user/" + userId);
            HttpURLConnection con3 = (HttpURLConnection) obj3.openConnection();
            con3.setRequestMethod("GET");
            con3.getResponseCode();

            shoppingCart = (ShoppingCart)rabbitTemplate.receiveAndConvert("cart-queue");

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return shoppingCart;
    }
    private List<Order> checkoutFallback(Long id){logger.info("checkoutFallback");return null;}
    @HystrixCommand(fallbackMethod = "checkoutFallback" )
    @RequestMapping(value = "/{id}/order", method = RequestMethod.GET)
    public List<Order> checkout(@PathVariable("id") Long id){
        List<Order> order=  null;

        try {
            URL obj = new URL("http://localhost:9999/carts/" + id);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.getResponseCode();

            ShoppingCart cart = (ShoppingCart)rabbitTemplate.receiveAndConvert("cart-queue");

            URL obj2 = new URL("http://localhost:9999/carts/" + id + "/order");
            HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();
            con2.setRequestMethod("POST");
            con2.getResponseCode();



            URL obj3 = new URL("http://localhost:9999/orders/user?id="+ cart.getUserid());
            HttpURLConnection con3 = (HttpURLConnection) obj3.openConnection();
            con3.setRequestMethod("GET");
            con3.getResponseCode();

             order = (List<Order>)rabbitTemplate.receiveAndConvert("order-queue");

            logger.info("Received from order-queue order:" + order);


        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return order;
    }
    private ShoppingCart newShoppingCartFallback(Long userId){logger.info("newShoppingCartFallback");return null;}
    @HystrixCommand(fallbackMethod = "newShoppingCartFallback" )
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public ShoppingCart newShoppingCart(@Validated @RequestParam("userId") Long userId){
        ShoppingCart shoppingCart = null;
        try {
            URL obj = new URL("http://localhost:9999/carts/user/" + userId);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.getResponseCode();

            URL obj2 = new URL("http://localhost:9999/carts/user/" + userId);
            HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();
            con2.setRequestMethod("GET");
            con2.getResponseCode();


            shoppingCart = (ShoppingCart)rabbitTemplate.receiveAndConvert("cart-queue");


            logger.info("Shopping cart created: " + shoppingCart);

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return shoppingCart;
    }

    private ShoppingCart receiveShoppingCartById(Long id){
        ShoppingCart cart = null;

        try {
            URL obj = new URL("http://localhost:9999/carts/" + id);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.getResponseCode();

            cart = (ShoppingCart) rabbitTemplate.receiveAndConvert("cart-queue");

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return cart;
    }

    private ShoppingCart receiveShoppingCartByUserId(Long id){
        ShoppingCart cart = null;

        try {
            URL obj = new URL("http://localhost:9999/carts/user/" + id);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.getResponseCode();

            cart = (ShoppingCart) rabbitTemplate.receiveAndConvert("cart-queue");

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return cart;
    }

}
