package com.uiservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shopcommon.model.Product;
import com.uiservice.messaging.ProductProcessor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Flaviu Cicio on 13.07.2016.
 */
@RestController
@RequestMapping(value = "/api/products")
@EnableCaching
public class ProductUiController {
    Logger logger;

    /*******
     * CHANNELS
     *******/
    @Autowired
    @Qualifier(ProductProcessor.OUTPUT_DELETE)
    private MessageChannel channel_delete;
    @Autowired
    @Qualifier(ProductProcessor.OUTPUT_CREATE)
    private MessageChannel channel_create;
    /********/

    @Autowired
    private LoadBalancerClient loadBalancer;


    /**
     * USED TO MAKE REST CALLS
     **/
    RestTemplate restTemplate;

    public ProductUiController() {
        logger = Logger.getLogger(ProductUiController.class);
        restTemplate = new RestTemplate();
    }

    /**
     * returns all the products in the database
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "getAllProductsFallback")
    @Cacheable(value = "products")
    public Product[] getAllProducts() {
        ServiceInstance instance = loadBalancer.choose("product-service");
        return restTemplate.getForObject(instance.getUri() + "/products", Product[].class);
    }

    /**
     * Returns a specific product
     *
     * @param id
     * @return
     */
    @Cacheable(value = "product")
    @HystrixCommand(fallbackMethod = "getProductFallback")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(@PathVariable("id") Long id) {
        ServiceInstance instance = loadBalancer.choose("product-service");
        return restTemplate.getForObject(instance.getUri() + "/products/" + id, Product.class);
    }

    /**
     * Add a product
     *
     * @param product
     * @return
     */
    @HystrixCommand(fallbackMethod = "addProductFallback")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "product", key = "#product.id")})
    public Product addProduct(@RequestBody Product product) {
        logger.info("SENT VIA OUTPUT_CREATE: " + product);
        channel_create.send(MessageBuilder.withPayload(product).build());
        return product;
    }

    /**
     * Delete a product
     *
     * @param id
     */
    @HystrixCommand(fallbackMethod = "removeProductFallback")
    @RequestMapping(method = RequestMethod.DELETE)
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "product")})
    public void removeProduct(@RequestParam("id") Long id) {
        logger.info("SENT VIA OUTPUT_DELETE: " + id);
        this.channel_delete.send(MessageBuilder.withPayload(id).build());
    }


    /**************************************/
    /**********FALLBACK METHODS************/
    /**************************************/

    /**
     * Fallback method for getAllProducts
     *
     * @return
     */
    Product[] getAllProductsFallback() {
        logger.info("getAllProductsFallback");
        return null;
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

    /**
     * Fallback method for getProduct
     *
     * @param id
     * @return
     */
    Product getProductFallback(Long id) {
        logger.warn("getProductFallback");
        return null;
    }

}
