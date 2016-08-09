package com.productservice.controller;

import com.productservice.messaging.ProductProcessor;
import com.productservice.service.ProductService;
import com.shopcommon.model.Product;
import com.shopcommon.model.User;
import com.sun.media.jfxmedia.Media;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Flaviu Cicio on 06.07.2016.
 */
@RestController
@RequestMapping(value = "/products")
@EnableBinding(ProductProcessor.class)
public class ProductController {
    Logger logger = Logger.getLogger(ProductController.class);

    @Autowired
    RabbitTemplate template;

    @Autowired
    ProductService productService;

    /**
     * Returns all products
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProducts(){
        List<Product> products = productService.getAll();
        logger.info("Requested via Rest API all products");
        return products;
    }

    /**
     * Return a specific product
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(@PathVariable("id") Long id) {
        Product product = productService.getById(id);
        logger.info("Requested via Rest API product: " + product);
        return product;
    }

    /**
     * Delete from the database product with given ID
     *
     * @param id
     */
    @StreamListener(ProductProcessor.INPUT_DELETE)
    public void delete(Long id) {
        logger.info("RECEIVED FROM OUTPUT_DELETE: " + id);
        productService.delete(id);
    }


    /**
     * Updates a product received from RabbitMQ
     *
     * @return
     */
    @StreamListener(ProductProcessor.INPUT_UPDATE)
    public void update(Product product) {
        logger.info("RECEIVED FROM OUTPUT_UPDATE: " + product);
        productService.update(product);
    }

    /**
     * Creates a new product received from RabbitMQ
     *
     * @return
     */
    @StreamListener(ProductProcessor.INPUT_CREATE)
    public void create(Product product) {
        logger.info("RECEIVED FROM INPUT_CREATE: " + product);
        productService.create(product);
    }
}
