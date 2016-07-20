package com.uiservice.controller;

import com.shopcommon.model.Product;
import com.shopcommon.model.User;
import org.apache.log4j.Logger;
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
 * Created by Flaviu Cicio on 13.07.2016.
 */

@RestController
@RequestMapping(value = "/api/users")
public class UserUiController {

    Logger logger = Logger.getLogger(ProductUiController.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAllUsers(){

        List<User> users = new ArrayList<>();

        try {
            URL obj = new URL("http://localhost:9999/users");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.getResponseCode();

            users.addAll((List<User>) rabbitTemplate.receiveAndConvert("user-queue"));

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return users;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable("id") Long id){
        User user = receiveUserById(id);

        return user;
    }


    private User receiveUserById(Long id){
        User user = null;

        try {
            URL obj = new URL("http://localhost:9999/users/" + id);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.getResponseCode();

            user = (User) rabbitTemplate.receiveAndConvert("user-queue");

        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return user;
    }
}
