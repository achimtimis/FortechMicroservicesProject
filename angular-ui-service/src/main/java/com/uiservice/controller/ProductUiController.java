package com.uiservice.controller;

import com.shopcommon.model.Product;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping(value = "/api/products")
public class ProductUiController {

    Logger logger = Logger.getLogger(ProductUiController.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @RequestMapping(method = RequestMethod.GET)
    public List<Product> getAllProducts(){

        List<Product> products = new ArrayList<>();

        try {
            rabbitAdmin.purgeQueue("product-queue", false);

            URL obj = new URL("http://localhost:9999/product-service/products");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.getResponseCode();

            products.addAll((List<Product>) rabbitTemplate.receiveAndConvert("product-queue"));

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return products;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product addProduct(Product product){
        Product products = null;

        try {
            rabbitAdmin.purgeQueue("product-queue", false);

            rabbitTemplate.convertAndSend("product-queue", product);

            URL obj = new URL("http://localhost:9999/product-service/products");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.getResponseCode();

            product = (Product)con.getContent();

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return product;
    }




}
