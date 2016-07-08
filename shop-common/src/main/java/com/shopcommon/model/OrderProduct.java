package com.shopcommon.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

/**
 * Created by Flaviu Cicio on 07.07.2016.
 */
@Entity
@Table(name = "order_product", schema = "orders")
public class OrderProduct {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne
    @JoinColumn(name="ORDER_ID")
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Order byOrder;

    private Long productId;

    private String productName;

    private int productPrice;

    private int quantity;

    public OrderProduct() {
    }

    public OrderProduct(Long id, Order order, Long productId, String productName, int productPrice, int quantity) {
        this.id = id;
        this.byOrder = order;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Order getOrder() {
        return byOrder;
    }

    public void setOrder(Order order) {
        this.byOrder = order;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
//                ", orderId=" + byOrder.getId() +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", quantity=" + quantity +
                '}';
    }
}
