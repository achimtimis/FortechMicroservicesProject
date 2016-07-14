package com.uiservice.controller;

import com.shopcommon.model.ShoppingCart;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/carts")
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ShoppingCart getShoppingCartById(@PathVariable("id") Long id){
        ShoppingCart cart = receiveShoppingCartById(id);

        return cart;
    }

    @RequestMapping(value = "/{id}/products", method = RequestMethod.POST)
    public ShoppingCart addProductToShoppingCart(@PathVariable("id") Long id, @RequestParam(value = "productId") Long productId,@RequestParam(value = "quantity") int quantity){
        ShoppingCart shoppingCart = null;

        try {
            URL obj = new URL("http://localhost:9999/carts/" + id + "/products?" + "productId=" + productId + "&quantity=" + quantity );
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.getResponseCode();

            URL obj2 = new URL("http://localhost:9999/carts/" + id);
            HttpURLConnection con2 = (HttpURLConnection) obj.openConnection();
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

}
