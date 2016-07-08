package com.orderservice.service;

import com.orderservice.model.Order;
import com.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Flaviu Cicio on 07.07.2016.
 */
@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public List<Order> get(){
        return orderRepository.findAll();
    }

    public Order getById(Long id){
        return orderRepository.findOne(id);
    }

    public Order create(Order order){
        return orderRepository.saveAndFlush(order);
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
}
