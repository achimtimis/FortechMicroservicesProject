package com.integration.test;

import com.integration.test.repository.ProductRepository;
import com.shopcommon.model.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flaviu Cicio on 27.07.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class ProductServiceIntegrationTest extends IntegrationTests{
    private static final String TEST_PRODUCT_NAME = "productTest";
    private static final int TEST_PRODUCT_PRICE = 20;
    private static final int TEST_PRODUCT_STOCK = 100;
    private Product product;
    public static final String API_URL = "http://localhost:9999/api";

    @Autowired
    ProductRepository productRepository;

    @Before
    public final void setUpProductTest() throws Exception{
        product = new Product(TEST_PRODUCT_NAME, TEST_PRODUCT_STOCK, TEST_PRODUCT_PRICE);
    }

    @Test
    public void testGetAllProducts(){

        //Generate products, add them to repository and save their ids
        List<Long> testProductIds = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Product testProduct = new Product(TEST_PRODUCT_NAME + String.valueOf(i), i*10, i);
            testProductIds.add(productRepository.save(testProduct).getId());
        }

        Product[] apiProducts = restTemplate.getForObject(API_URL + "/products", Product[].class);
        List<Product> repoProducts = productRepository.findAll();

        Assert.assertArrayEquals(apiProducts, repoProducts.toArray());

        testProductIds.forEach(id -> productRepository.delete(id));
    }

    @Test
    public void testGetSpecificProduct(){
        Product repoProduct = productRepository.save(this.product);

        Product apiProduct = restTemplate.getForObject(API_URL + "/products/" + repoProduct.getId(), Product.class);

        Assert.assertEquals(apiProduct, repoProduct);

        productRepository.delete(repoProduct.getId());
    }

    @Test
    public void testAddProduct(){

        int preAddSize = productRepository.findAll().size();

        HttpEntity<Product> request = new HttpEntity<>(product, headers);
        Product apiProduct = restTemplate.postForObject(API_URL + "/products", request, Product.class);
        Product repoProduct = productRepository.findOne(apiProduct.getId());

        int postAddSize = productRepository.findAll().size();


        Assert.assertTrue(preAddSize < postAddSize);
        Assert.assertEquals(repoProduct, apiProduct);
        Assert.assertEquals(repoProduct.getName(), product.getName());
        Assert.assertEquals(repoProduct.getPrice(), product.getPrice());
        Assert.assertEquals(repoProduct.getStock(), product.getStock());

        productRepository.delete(repoProduct.getId());
    }

    @Test
    public void testDeleteProduct(){

        int initialSize = productRepository.findAll().size();

        Product repoProduct = productRepository.save(product);

        int postAddSize = productRepository.findAll().size();

        restTemplate.delete(API_URL + "/products?id=" + repoProduct.getId());

        int postDeleteSize = productRepository.findAll().size();

        Assert.assertTrue(initialSize < postAddSize);
        Assert.assertTrue(initialSize == postDeleteSize);
    }
}
