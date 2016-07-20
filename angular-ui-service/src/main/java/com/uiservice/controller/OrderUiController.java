package com.uiservice.controller;

import com.shopcommon.model.Order;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Flaviu Cicio on 13.07.2016.
 */
@RestController
@RequestMapping(value = "/api/orders")
public class OrderUiController {

    Logger logger = Logger.getLogger(OrderUiController.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<Order> getOrderByUserId(@RequestParam("id") Long userId){
        List<Order> order = null;

        try {
            URL obj2 = new URL("http://localhost:9999/orders/user?id=" + userId);
            HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();
            con2.setRequestMethod("GET");
            con2.getResponseCode();

            order = (List<Order>)rabbitTemplate.receiveAndConvert("order-queue");

            logger.info("Received from order-queue order: " + order);

            return order;
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        logger.info("No Order found for user id: " + userId);
        return order;
    }
}
