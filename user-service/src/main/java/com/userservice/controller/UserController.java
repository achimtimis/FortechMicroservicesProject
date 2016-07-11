package com.userservice.controller;

import com.shopcommon.model.User;
import com.userservice.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Achim Timis on 7/6/2016.
 */

@RefreshScope
@RestController
public class UserController {


    Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    RabbitTemplate template;

    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "users",method= RequestMethod.GET)
    public List<User> findAll(){
        return this.userRepository.findAll();
    }

//    @RequestMapping(value = "users/{id}",method= RequestMethod.GET)
//    public User get(@PathVariable Long id){
//        return  userRepository.findOne(id);
//    }

    @RequestMapping(value = "users", method = RequestMethod.POST)
    public User create(@RequestBody User user){
        return userRepository.saveAndFlush(user);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.DELETE)
    public User delete(@PathVariable Long id){
        User existingUser=userRepository.findOne(id);
        userRepository.delete(existingUser);
        return existingUser;
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.PUT)
    public User update(@PathVariable Long id,@RequestBody User user){
        User existingUser = userRepository.findOne(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        userRepository.save(existingUser);
        return existingUser;



    }

//        @Value("${message}")
//        private String message;
//
//        @RequestMapping("message")
//        String getMessage(){
//            return this.message;
//    }


    @RequestMapping("users/{id}")
    public String getUser(@PathVariable("id") Long id){

        User existingUser = userRepository.findOne(id);

        logger.info("Emit to user-queue:" + existingUser);
        template.convertAndSend(existingUser);

        return "Emit to user-queue";
    }


}
