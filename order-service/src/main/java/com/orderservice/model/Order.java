package com.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by Flaviu Cicio on 07.07.2016.
 */
@Entity
@Table(name = "t_order")
public class Order {

    @Id
    private Long id;

    private Long userId;

    @OneToMany(mappedBy="byOrder", cascade = CascadeType.ALL)
    private List<OrderProduct> products;

    private LocalDateTime date;

    public Order() {
    }

    public Order(Long id, Long userId, List<OrderProduct> products, LocalDateTime date) {

        this.id = id;
        this.userId = userId;
        this.products = products;
        this.date = date;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date +
                '}';
    }

}
