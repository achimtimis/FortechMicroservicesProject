package com.userservice.controller;

import com.shopcommon.model.User;
import com.userservice.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Achim Timis on 7/6/2016.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RefreshScope
@RestController
@RequestMapping("/users")
public class UserController {


    Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    RabbitTemplate template;

    @Autowired
    AmqpAdmin rabbitAdmin;

    @Autowired
    private UserRepository userRepository;

    /**
     * returns the list with all users
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    public List<User> findAll(){
        rabbitAdmin.purgeQueue("user-queue", false);

        List<User> users = userRepository.findAll();

        template.convertAndSend("user-queue", users);

        logger.info("Sent " + users.size() + " users via user-queue");

        return users;
    }

    /**
     * adds the received user to the database
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public User create(){

        User user = (User)template.receiveAndConvert("user-queue");

        if(user != null){
            logger.info("User created: " + user);
            return userRepository.saveAndFlush(user);
        }
        return null;
    }

    /**
     * deletes the user with the given id
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public User delete(@PathVariable Long id){
        User existingUser=userRepository.findOne(id);
        userRepository.delete(existingUser);
        return existingUser;
    }

    /**
     * updates the user with the given id
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public User update(@PathVariable Long id){
        User existingUser = userRepository.findOne(id);
        User newUser = (User)template.receiveAndConvert("user-queue");
        existingUser.setUsername(newUser.getUsername());
        existingUser.setPassword(newUser.getPassword());
        userRepository.save(existingUser);

        return existingUser;

    }

    /**
     * returns the user of a given id
     * @param id
     * @return
     */
    @RequestMapping("/{id}")
    public String getUser(@PathVariable("id") Long id){

        User existingUser = userRepository.findOne(id);

        logger.info("Emit to user-queue: " + existingUser);
        template.convertAndSend(existingUser);

        return "Emit to user-queue";
    }

}
