package com.orderservice.controller;

import com.orderservice.service.OrderService;
import com.shopcommon.model.Order;
import com.shopcommon.model.OrderProduct;
import com.shopcommon.model.Product;
import com.shopcommon.model.User;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpAdmin;
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
 * Created by Flaviu Cicio on 07.07.2016.
 */
@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    Logger logger = Logger.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin rabbitAdmin;

    /**
     * Send all orders via order-queue
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Order> getOrders(){
        List<Order> orders = orderService.get();

        rabbitTemplate.convertAndSend("order-queue", orders);

        logger.info("A number of " + orders.size() + " orders was sent via order-queue");

        return orders;
    }

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public Order getOrderByUser(@RequestParam(name = "id") Long id){
        Order order = orderService.getByUserId(id);

        rabbitTemplate.convertAndSend("order-queue", order);



        return order;
    }

    /**
     * Find an order with a spefic id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Order getOrder(@PathVariable("id") Long id){

        Order order = orderService.getById(id);
        if(order != null){
            rabbitTemplate.convertAndSend("order-queue", order);

            logger.info("Order " + order + " was sent via order-queue");
            return order;
        }
        logger.info("No order was found for id " + id);
        return order;
    }

    /**
     * Persist a new order received from amqp messaging
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Order createOrder(){

        Order order = (Order) rabbitTemplate.receiveAndConvert("order-queue");

        logger.info("Created a new order: " +  order);

        return orderService.create(order);
    }

    /**
     * Delete a specific order
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteOrder(@PathVariable("id") Long id){
        logger.info("Order with id " + id + " was deleted");
        orderService.delete(id);
    }



    /**
     * Get user from a specific order
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    public User getOrderUser(@PathVariable("id") Long id){
        Order order = orderService.getById(id);

        rabbitAdmin.purgeQueue("user-queue", false);

        try {
            logger.info("Requested from user-service user from order with id " +  id);
            logger.info("USER ID " +  order.getUserId());

            URL obj = new URL("http://localhost:9999/user-service/users/" + order.getUserId());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.getResponseCode();


            User user = (User)rabbitTemplate.receiveAndConvert("user-queue");

            logger.info("Received from user-service: " +  user);

            return user;

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("No user found!");
        return null;
    }

    /**
     * Get products from a specific order
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/products", method = RequestMethod.GET)
    public List<Product> getOrderProducts(@PathVariable("id") Long id){

        rabbitAdmin.purgeQueue("product-queue", false);

        List<Product> products = new ArrayList<>();

        logger.info("Requested from product-service products from order with id " +  id);

        Order order = orderService.getById(id);

        order.getProducts().forEach(orderProduct -> {
            try {
                URL obj = new URL("http://localhost:9999/product-service/products/" + orderProduct.getProductId());
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.getResponseCode();

                products.add((Product) rabbitTemplate.receiveAndConvert("product-queue"));

            } catch (MalformedURLException e) {
                logger.error(e.getMessage());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        });

        logger.info("Received from product-service a number of " +  products.size() + " products");

        return products;
    }


}
