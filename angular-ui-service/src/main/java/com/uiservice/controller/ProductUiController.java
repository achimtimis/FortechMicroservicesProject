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

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @RequestMapping(method = RequestMethod.GET)
    public List<Product> getAllProducts(){
        Logger logger = Logger.getLogger(ProductUiController.class);

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




}
