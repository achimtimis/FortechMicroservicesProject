package com.productservice.controller;

import com.productservice.messaging.ProductProcessor;
import com.productservice.service.ProductService;
import com.shopcommon.model.Product;
import com.shopcommon.model.User;
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
     * Sends via RabbitMQ all products from the database
     *
     * @return
     */
//    @RequestMapping(method = RequestMethod.GET)
//    public List<Product> getProducts(){
//
//        List<Product> products = productService.getAll();
//
//        logger.info("Emit to product-queue: " + products);
//
//        template.convertAndSend("product-queue", products);
//
//        return products;
//    }


    @Autowired
    @Output(ProductProcessor.OUTPUT_GETALL)
    private MessageChannel channel_getAll;

    @RequestMapping(method = RequestMethod.GET)
    public void getAllProductsViaMessage() {
        List<Product> products = productService.getAll();
        logger.info("SENT VIA OUTPUT_GETALL: " + products);
        channel_getAll.send(MessageBuilder.withPayload(products).build());
    }


    /**
     * Delete from the database product with given ID
     *
     * @param id
     */
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    public void deleteProduct(@PathVariable("id") Long id){
//        logger.info("Delete from database product with id: " + id);
//
//        productService.delete(id);
//    }
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
//    @RequestMapping(method = RequestMethod.PUT)
//    public Product updateProduct() {
//        Product product = (Product) template.receiveAndConvert("product-queue");
//
//        logger.info("Received from product-queue: " + product);
//        logger.info("Updated product in the database");
//
//        return productService.update(product);
//    }
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
//    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public Product createProduct() {
//
//        Product product = (Product) template.receiveAndConvert("product-queue");
//
//        logger.info("Received from product-queue: " + product);
//
//        return productService.create(product);
//    }
    @StreamListener(ProductProcessor.INPUT_CREATE)
    public void create(Product product) {
        logger.info("RECEIVED FROM INPUT_CREATE: " + product);
        productService.create(product);
    }


    /**
     * Sends a requested product via rabbit messaging
     *
     * @param id
     * @return
     */
//    @RequestMapping("/{id}")
//    public String getProduct(@PathVariable("id") Long id) {
//
//        Product existingProduct = productService.getById(id);
//
//        logger.info("Emit to product-queue:" + existingProduct);
//        template.convertAndSend(existingProduct);
//
//        return "Emit to product-queue";
//    }
    @Autowired
    @Output(ProductProcessor.OUTPUT_GET)
    private MessageChannel channel_get;


    @RequestMapping("/{id}")
    public void get(@PathVariable("id") Long id) {
        Product existingProduct = productService.getById(id);
        logger.info("SENT VIA OUTPUT_GET: " + existingProduct);
        this.channel_get.send(MessageBuilder.withPayload(existingProduct).build());
    }

}
