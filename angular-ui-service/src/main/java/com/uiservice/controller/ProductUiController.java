package com.uiservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shopcommon.model.Product;
import com.uiservice.messaging.ProductProcessor;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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

import java.io.IOException;
import java.net.*;
import java.util.List;

/**
 * Created by Flaviu Cicio on 13.07.2016.
 */
@RestController
@RequestMapping(value = "/api/products")
public class ProductUiController {

    /*******
     * LISTENERS
     *******/
    private List<Product> listenAllProducts;
    private Product listenProduct;

    /*******
     * CHANNELS
     *******/
    @Autowired
    @Qualifier(ProductProcessor.OUTPUT_DELETE)
    private MessageChannel channel_delete;
    @Autowired
    @Qualifier(ProductProcessor.OUTPUT_CREATE)
    private MessageChannel channel_create;

    Logger logger = Logger.getLogger(ProductUiController.class);


    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Autowired
    private LoadBalancerClient loadBalancer;

    /**
     * returns all the products in the database
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "getAllProductsFallback")
    public List<Product> getAllProducts() {
        try {
            ServiceInstance instance = loadBalancer.choose("product-service");
            URI uri = instance.getUri();
            URL obj = new URL(uri.toString() + "/products");

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.getResponseCode();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return this.listenAllProducts;
    }

    /**
     * Returns a specific product from product-service
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(@PathVariable("id") Long id) {
        try {
            ServiceInstance instance = loadBalancer.choose("product-service");
            URI uri = instance.getUri();

            URL obj = new URL(uri.toString() + "/products/" + id);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.getResponseCode();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return listenProduct;
    }

    @HystrixCommand(fallbackMethod = "addProductFallback")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product addProduct(@RequestBody Product product) {

        logger.info("SENT VIA OUTPUT_CREATE: " + product);
        channel_create.send(MessageBuilder.withPayload(product).build());

        return product;
    }


    @HystrixCommand(fallbackMethod = "removeProductFallback")
    @RequestMapping(method = RequestMethod.DELETE)
    public void removeProduct(@RequestParam("id") Long id) {

        logger.info("SENT VIA OUTPUT_DELETE: " + id);
        this.channel_delete.send(MessageBuilder.withPayload(id).build());
    }


    /**************************************/
    /**********LISTENERS*******************/
    /**************************************/

    /**
     * Receiver for all products
     *
     * @param products
     */
    @StreamListener(ProductProcessor.INPUT_GETALL)
    public void listenerGetAllProducts(List<Product> products) {
        logger.info("RECEIVED FROM INPUT_GETALL: " + products);
        listenAllProducts = products;
    }

    /**
     * Receiver for product
     *
     * @param product
     */
    @StreamListener(ProductProcessor.INPUT_GET)
    public void listenerGetProduct(Product product) {
        logger.info("RECEIVED FROM INPUT_GET: " + product);
        listenProduct = product;
    }


    /**************************************/
    /**********FALLBACK METHODS************/
    /**************************************/

    /**
     * Fallback method for getAllProducts
     *
     * @return
     */
    public List<Product> getAllProductsFallback(Throwable e) {
        logger.info("getAllProductsFallback");
        return listenAllProducts;
    }

    /**
     * Fallback method for addProduct
     *
     * @param product
     * @return
     */
    Product addProductFallback(Product product) {
        logger.info("addProductFallback");
        return null;
    }

    /**
     * Fallback method for delete
     *
     * @param id
     */
    void removeProductFallback(Long id) {
        logger.info("removeProductFallback");
    }

}
