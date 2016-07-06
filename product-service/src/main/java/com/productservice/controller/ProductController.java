package com.productservice.controller;

import com.productservice.model.Product;
import com.productservice.repository.ProductRepository;
import com.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Flaviu Cicio on 06.07.2016.
 */

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> getProducts(){
        return productService.get();
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public Product getProduct(@PathVariable("id") Long id){
        return productService.getById(id);
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public Product createProduct(@RequestBody Product product){
        return productService.create(product);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable("id") Long id){
        productService.delete(id);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    public Product updateProduct(@PathVariable("id") Long id, @RequestBody Product product){
        return productService.update(product);
    }

}
