package com.integration.test;

import com.integration.test.repository.ProductRepository;
import com.shopcommon.model.Product;
import com.sun.xml.internal.bind.v2.TODO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest("server.port:0")
@SpringApplicationConfiguration(classes = {IntegrationTestApplication.class})
public class IntegrationTests {
    RestTemplate restTemplate;
    MultiValueMap<String, String> headers;

    @Before
    public final void setUp() throws Exception{
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
    }
}
