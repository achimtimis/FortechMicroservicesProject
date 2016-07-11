//package com.orderservice.messaging;
//
//import com.shopcommon.model.Product;
//import org.apache.log4j.Logger;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Controller
//public class SampleController {
//    Logger logger = Logger.getLogger(SampleController.class);
//
//    @Autowired
//    AmqpTemplate template;
//
//    @RequestMapping("/emit")
//    @ResponseBody
//    String queue2() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setName("productName");
//        product.setStock(100);
//
//        logger.info("Emit to queue2");
//        template.convertAndSend(product);
//        return "Emit to queue 2";
//    }
//
//    @RequestMapping("/receive")
//    String queue2Receive(){
//        Product product = (Product)template.receiveAndConvert("queue2");
//
//
//        logger.info("Receive from queue 2: " + product);
//        return "Receive from queue 2";
//    }
//}