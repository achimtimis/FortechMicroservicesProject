package com.integration.test;

import com.shopcommon.model.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Flaviu Cicio on 27.07.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {IntegrationTestApplication.class})
public class ProductServiceIntegrationTest extends IntegrationTests{
    private static final String TEST_PRODUCT_NAME = "productTest";
    private static final int TEST_PRODUCT_PRICE = 20;
    private static final int TEST_PRODUCT_STOCK = 100;
    private List<Product> testProducts;
    public static final String API_URL = "http://localhost:9999/api";

@Before
    public final void setUpProductTest() throws Exception{
        testProducts = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Product product = new Product(TEST_PRODUCT_NAME + String.valueOf(i), TEST_PRODUCT_STOCK*i, TEST_PRODUCT_PRICE*i);
            testProducts.add(product);
        }
    }

    @Test
    public void testGetAllProducts(){

        List<Product> repoProducts = new ArrayList<>();

        testProducts.forEach(product -> repoProducts.add(this.addProduct(product)));
        List<Product> resultProducts = this.getAllProducts();

        //Asserts
        repoProducts.forEach(product ->  Assert.assertTrue(resultProducts.contains(product)));
        Assert.assertTrue(resultProducts.size() >= repoProducts.size());

        //Database cleaning
        repoProducts.forEach(product -> this.deleteProduct(product.getId()));
    }

    @Test
    public void testGetSpecificProduct(){

        Product repoProduct = this.addProduct(testProducts.get(0));
        Product apiProduct = this.getSpecificProduct(repoProduct.getId());

        Assert.assertEquals(apiProduct, repoProduct);

        this.deleteProduct(repoProduct.getId());
    }

    @Test
    public void testAddProduct(){

        Assert.assertFalse(this.getAllProducts().contains(this.testProducts.get(0)));

        Product product = this.addProduct(this.testProducts.get(0));

        Assert.assertTrue(this.getAllProducts().contains(product));

        this.deleteProduct(product.getId());
    }

    @Test
    public void testDeleteProduct(){

        Product product = this.addProduct(this.testProducts.get(0));
        Assert.assertTrue(this.getAllProducts().contains(product));

        this.deleteProduct(product.getId());
        Assert.assertFalse(this.getAllProducts().contains(product));

    }

    private Product addProduct(Product product){
        HttpEntity<Product> request = new HttpEntity<>(product, headers);
        Product apiProduct = restTemplate.postForObject(API_URL + "/products", request, Product.class);

        return apiProduct;
    }

    private void deleteProduct(Long id){
        restTemplate.delete(API_URL + "/products?id=" + id);
    }

    private List<Product> getAllProducts(){
        Product[] apiProducts = restTemplate.getForObject(API_URL + "/products", Product[].class);
        return Arrays.asList(apiProducts);
    }

    private Product getSpecificProduct(Long id){
        Product product = restTemplate.getForObject(API_URL + "/products/" + id, Product.class);
        return product;
    }
}
