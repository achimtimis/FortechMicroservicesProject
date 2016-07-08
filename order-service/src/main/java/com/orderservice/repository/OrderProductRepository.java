package com.orderservice.repository;

import com.shopcommon.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Flaviu Cicio on 12.07.2016.
 */
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>{
}
