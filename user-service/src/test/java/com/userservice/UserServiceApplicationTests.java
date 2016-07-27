package com.userservice;

import com.shopcommon.model.User;
import com.userservice.controller.UserController;
import com.userservice.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { UserServiceApplication.class, TestMockito.class})
@WebAppConfiguration
public class UserServiceApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @InjectMocks
    private UserController userController;

    @Mock
    RabbitAdmin rabbitAdmin;

    @Mock
    private UserRepository userRepository;

    @Before
    public final void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    public void testGetAllUserController() {

        User user1 = new User();
        user1.setId(1L);
        user1.setPassword("mockpassword1");
        user1.setUsername("mockusername1");

        User user2 = new User();
        user2.setId(2L);
        user2.setPassword("mockpassword2");
        user2.setUsername("mockusername2");

        User user3 = new User();
        user3.setId(3L);
        user3.setPassword("mockpassword3");
        user3.setUsername("mockusername3");

        User[] users = {user1, user2, user3};


        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(users));

        try {

            mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                    .andExpect(status().isOk());


            ArrayList<LinkedHashMap> result = (ArrayList<LinkedHashMap>) rabbitTemplate.receiveAndConvert("user-queue");

            List<User> usersResult = new ArrayList<>();
            result.forEach(user -> usersResult.add(new User(Long.parseLong(user.get("id").toString()), (String)user.get("username"),(String) user.get("password"))));

            Assert.assertArrayEquals(usersResult.toArray(), users);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDeleteASpecificUser(){
        User user1 = new User();
        user1.setId(1L);
        user1.setPassword("mockpassword1");
        user1.setUsername("mockusername1");

        Mockito.when(userRepository.findOne(1L)).thenReturn(user1);

        try {
            mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, "/users/" + user1.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.username", is("mockusername1")))
                    .andExpect(jsonPath("$.password", is("mockpassword1")))
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateProduct(){
        User user1 = new User();
        user1.setId(1L);
        user1.setPassword("mockpassword1");
        user1.setUsername("mockusername1");

        Mockito.when(userRepository.saveAndFlush(user1)).thenReturn(user1);

        rabbitTemplate.convertAndSend(user1);

        try {
            mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, "/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.username", is("mockusername1")))
                    .andExpect(jsonPath("$.password", is("mockpassword1")))
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}