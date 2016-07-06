package com.productservice.repository;

import com.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Flaviu Cicio on 06.07.2016.
 */
public interface ProductRepository extends JpaRepository<Product,Long> {
}
