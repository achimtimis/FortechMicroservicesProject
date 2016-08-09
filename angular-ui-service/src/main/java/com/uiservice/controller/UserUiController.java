package com.uiservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shopcommon.model.Product;
import com.shopcommon.model.User;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    Logger logger;

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    RabbitTemplate rabbitTemplate;

    RestTemplate restTemplate;

    public UserUiController() {
        logger = Logger.getLogger(UserUiController.class);
        restTemplate = new RestTemplate();
    }

    /**
     * Return all the users from the database
     * @return
     */
    @HystrixCommand(fallbackMethod = "getUsersFallback" )
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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

    /**
     * returns the user with a given id
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "getUserByIdFallback" )
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable("id") Long id){
        User user = receiveUserById(id);
        return user;
    }


    /************************
     ****RECEIVER****
     ************************/
    /**
     * Receive a specific user from user service
     *
     * @param id
     * @return
     */
    private User receiveUserById(Long id){
        ServiceInstance instance = loadBalancer.choose("user-service");
        User user = restTemplate.getForObject(instance.getUri() + "/users/" + id, User.class);
        logger.info("Requested user " + user + " via Rest API");
        return user;
    }

    /************************
     ****FALLBACK METHODS****
     ************************/

    /**
     * Fallback method for getUsers
     *
     * @return
     */
    private List<User> getUsersFallback(){
        logger.warn("getUsersFallback");
        return null;
    }

    /**
     * Fallback method for getUserById
     *
     * @param id
     * @return
     */
    private User getUserByIdFallback(Long id){
        logger.warn("getUserByIdFallback");
        return null;
    }

}
