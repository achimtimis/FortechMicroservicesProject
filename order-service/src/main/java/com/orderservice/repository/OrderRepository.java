package com.orderservice.repository;

import com.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Flaviu Cicio on 07.07.2016.
 */
public interface OrderRepository extends JpaRepository<Order,Long> {
}
