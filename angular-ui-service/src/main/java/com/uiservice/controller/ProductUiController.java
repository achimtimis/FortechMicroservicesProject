package com.uiservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shopcommon.model.Product;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flaviu Cicio on 13.07.2016.
 */
@RestController
@RequestMapping(value = "/api/products")
public class ProductUiController {

    Logger logger = Logger.getLogger(ProductUiController.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Autowired
    private LoadBalancerClient loadBalancer;

    private List<Product> getAllProductsFallback(){
        rabbitAdmin.purgeQueue("product-queue", true);
        return new ArrayList<Product>();
    }

    /**
     * returns all the products in the database
     * @return
     */
    @HystrixCommand(fallbackMethod = "getAllProductsFallback" )
    @RequestMapping(method = RequestMethod.GET)
    public List<Product> getAllProducts(){

        List<Product> products = new ArrayList<>();

        try {
//            rabbitAdmin.purgeQueue("product-queue", false);

            ServiceInstance instance = loadBalancer.choose("product-service");
            URI uri = instance.getUri();

//            URL obj = new URL("http://localhost:9999/product-service/products");
            URL obj = uri.toURL();
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

    /**
     * adds/updates the product to the database
     * @param product
     * @return
     */
    private Product addProductFallback(Product product){logger.info("addProductFallback");return null; }

    @HystrixCommand(fallbackMethod = "addProductFallback" )
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product addProduct(@RequestBody Product product){

        try {
//            rabbitAdmin.purgeQueue("product-queue", false);

            logger.info("Product " + product + " was send to product-queue");

            rabbitTemplate.convertAndSend("product-queue", product);

            URL obj = new URL("http://localhost:9999/product-service/products");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.getResponseCode();

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return product;
    }

    /**
     * removes the product given by its id from the database
     * @param id
     */
    private void removeProductFallback(Long id){logger.info("removeProductFallback");}
    @HystrixCommand(fallbackMethod = "removeProductFallback" )
    @RequestMapping(method = RequestMethod.DELETE)
    public void removeProduct(@RequestParam("id") Long id){
        try {
            URL obj = new URL("http://localhost:9999/product-service/products/" + id);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("DELETE");
            con.getResponseCode();

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
