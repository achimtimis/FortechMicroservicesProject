package com.orderservice.controller;

import com.orderservice.model.Order;
import com.orderservice.model.OrderProduct;
import com.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flaviu Cicio on 07.07.2016.
 */
@RestController
@RequestMapping(value = "/orders")
public class OrderController {

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
}
