package com.cartservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Achim Timis on 7/7/2016.
 */
@Entity
public class ShoppingCartProduct implements Serializable {

    @Id
    @GeneratedValue
    private Long id;



    @JsonIgnore
    public ShoppingCart getShoppingCart() {

        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id",nullable = true)
    private ShoppingCart shoppingCart;

    private Long productId;

    private int quantity;

    private String productName;



    private int productPrice;




    public ShoppingCartProduct() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ShoppingCartProduct(Long productId, int quantity, String productName, int productPrice) {

        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    @Override
    public boolean equals(Object o) {


        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShoppingCartProduct that = (ShoppingCartProduct) o;

        if (quantity != that.quantity) return false;
        if (productPrice != that.productPrice) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (shoppingCart != null ? !shoppingCart.equals(that.shoppingCart) : that.shoppingCart != null) return false;
        if (productId != null ? !productId.equals(that.productId) : that.productId != null) return false;
        return productName != null ? productName.equals(that.productName) : that.productName == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (shoppingCart != null ? shoppingCart.hashCode() : 0);
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + quantity;
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + productPrice;
        return result;
    }
    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }


}
