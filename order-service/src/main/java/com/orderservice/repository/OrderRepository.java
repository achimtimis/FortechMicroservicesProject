package com.orderservice.repository;


import com.shopcommon.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Flaviu Cicio on 07.07.2016.
 */
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> getByUserId(Long userId);
}
