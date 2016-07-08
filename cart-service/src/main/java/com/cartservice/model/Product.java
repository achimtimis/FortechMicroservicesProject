package com.cartservice.model;

import java.util.stream.Stream;

/**
 * Created by Achim Timis on 7/7/2016.
 */
public class Product {
    private Long id;
    private String name;
    private int stock;
    private int price;
    
//    // TODO: 7/8/2016 see if I should use the quantity here.atm im getting the quatity as a parameter in the controller 
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public Product(){}
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(Long id, String name, int stock, int price) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (stock != product.stock) return false;
        if (price != product.price) return false;
        if (id != null ? !id.equals(product.id) : product.id != null) return false;
        return name != null ? name.equals(product.name) : product.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + stock;
        result = 31 * result + price;
        return result;
    }
}
