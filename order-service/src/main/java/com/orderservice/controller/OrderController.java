package com.orderservice.controller;

import com.orderservice.service.OrderService;
import com.shopcommon.model.Order;
import com.shopcommon.model.OrderProduct;
import com.shopcommon.model.Product;
import com.shopcommon.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @RequestMapping(method = RequestMethod.GET)
    public List<Order> getOrders(){
        return orderService.get();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Order getOrder(@PathVariable("id") Long id){
        return orderService.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Order createOrder(@RequestBody Order order){

        List<OrderProduct> products = order.getProducts();
        products.stream().forEach(orderProduct -> orderProduct.setOrder(order));
        order.setProducts(products);
        return orderService.create(order);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteOrder(@PathVariable("id") Long id){
        orderService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Order updateOrder(@PathVariable("id") Long id, @RequestBody Order order){

        List<OrderProduct> products = order.getProducts();
        products.stream().forEach(orderProduct -> orderProduct.setOrder(order));
        order.setProducts(products);

        return orderService.update(order);
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    public User getOrderUser(@PathVariable("id") Long id){
        Order order = orderService.getById(id);
        try {
            logger.info("Requested from user-service user from order " +  id);

            User user = orderService.getOrderUser(order.getUserId());

            logger.info("Received from user-service: " +  user);

            return user;

        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("No user found!");
        return null;
    }


    @RequestMapping(value = "/{id}/products", method = RequestMethod.GET)
    public List<Product> getOrderProducts(@PathVariable("id") Long id){

        logger.info("Requested from product-service products from order " +  id);

        List<Product> products = orderService.getOrderProducts(id);

        logger.info("Received from product-service " +  products.size() + " products");

        return products;
    }

}
