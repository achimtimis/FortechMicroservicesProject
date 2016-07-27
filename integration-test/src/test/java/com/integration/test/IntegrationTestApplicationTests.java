package com.integration.test;

import com.integration.test.repository.ProductRepository;
import com.shopcommon.model.Product;
import com.sun.xml.internal.bind.v2.TODO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest("server.port:0")
@SpringApplicationConfiguration(classes = {IntegrationTestApplication.class})
public class IntegrationTestApplicationTests {

    RestTemplate restTemplate;

    private MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    @Before
    public final void setUp() throws Exception{
        restTemplate = new RestTemplate();
    }

    @Test
    public void testGetAllProducts(){
        Product[] apiProducts = restTemplate.getForObject("http://localhost:9999/api/products", Product[].class);
        List<Product> repoProducts = productRepository.findAll();

        Assert.assertArrayEquals(apiProducts, repoProducts.toArray());

    }

    @Test
    public void testGetSpecificProduct(){
        long product_test_id = 46L;
        //TODO : add product then find it;

        Product apiProduct = restTemplate.getForObject("http://localhost:9999/api/products?id=" + product_test_id, Product.class);
        Product repoProduct = productRepository.findOne(product_test_id);

        Assert.assertEquals(apiProduct, repoProduct);
    }

//    @Test
//    public void testAddAndDeleteProduct(){
//        Product product = new Product("productTest", 100, 20);
//        Assert.assert
//
//        int initialSize = restTemplate.getForObject("http://localhost:9999/api/products", Product[].class);
//    }

//    @Test
//    public void testDeleteProduct(){
//        restTemplate.delete("http://localhost:9999/api/products?id=");
//    }

}
