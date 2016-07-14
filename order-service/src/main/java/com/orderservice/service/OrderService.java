package com.orderservice.service;

import com.orderservice.repository.OrderProductRepository;
import com.orderservice.repository.OrderRepository;
import com.shopcommon.model.Order;
import com.shopcommon.model.Product;
import com.shopcommon.model.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flaviu Cicio on 07.07.2016.
 */
@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;


    public List<Order> get(){
        return orderRepository.findAll();
    }

    public Order getById(Long id){
        return orderRepository.findOne(id);
    }

    public Order create(Order order){
        Order o = orderRepository.saveAndFlush(order);

        order.getProducts().stream().forEach(orderProduct -> {
            orderProduct.setOrder(o);
            orderProductRepository.saveAndFlush(orderProduct);
        });
        return o;
    }

    public void delete(Long id){
        orderRepository.delete(id);
    }

    public Order update(Order order){
        if(orderRepository.findOne(order.getId()) != null){
            orderRepository.delete(order.getId());
            return orderRepository.save(order);

        }

        return null;
    }

    public Order getByUserId(Long id){
        return orderRepository.getByUserId(id);
    }


}
