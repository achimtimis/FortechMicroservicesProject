package com.orderservice.service;

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
    RabbitTemplate rabbitTemplate;

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

    public User getOrderUser(Long id) throws IOException {

        URL obj = new URL("http://localhost:9999/user-service/users/" + id);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        int responseCode = con.getResponseCode();

        return (User)rabbitTemplate.receiveAndConvert("user-queue");

    }

    public List<Product> getOrderProducts(Long id){

        Order order = orderRepository.findOne(id);
        List<Product> products = new ArrayList<>();

        order.getProducts().forEach(orderProduct -> {
            try {
                URL obj = new URL("http://localhost:9999/product-service/products/" + orderProduct.getProductId());
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.getResponseCode();

                products.add((Product) rabbitTemplate.receiveAndConvert("product-queue"));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        return products;
    }
}
