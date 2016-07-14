package com.cartservice.repository;

import com.shopcommon.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Achim Timis on 7/7/2016.
 */
@Transactional
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {

    ShoppingCart findByUserid(Long userid);
}
