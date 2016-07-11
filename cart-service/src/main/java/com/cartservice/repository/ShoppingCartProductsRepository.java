package com.cartservice.repository;

import com.shopcommon.model.ShoppingCartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Achim Timis on 7/7/2016.
 */
public interface ShoppingCartProductsRepository extends JpaRepository<ShoppingCartProduct,Long> {


}
