package com.integration.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@IntegrationTest("server.port:0")
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
