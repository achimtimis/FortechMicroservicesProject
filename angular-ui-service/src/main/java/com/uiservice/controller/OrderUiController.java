package com.uiservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Flaviu Cicio on 13.07.2016.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/api/orders")
public class OrderUiController {
}
