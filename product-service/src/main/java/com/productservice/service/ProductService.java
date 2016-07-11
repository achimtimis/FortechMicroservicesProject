package com.productservice.service;
import com.productservice.repository.ProductRepository;
import com.shopcommon.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Flaviu Cicio on 06.07.2016.
 */

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Product getById(Long id){
        return productRepository.findOne(id);
    }

    public Product create(Product product){
        return productRepository.saveAndFlush(product);
    }

    public void delete(Long id){
        productRepository.delete(id);
    }

    public Product update(Product product){
        if(productRepository.findOne(product.getId()) != null){
            productRepository.delete(product.getId());
            return productRepository.save(product);

        }

        return null;
    }

}
