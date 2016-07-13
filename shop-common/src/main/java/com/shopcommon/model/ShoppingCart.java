package com.shopcommon.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Achim Timis on 7/7/2016.
 */
@Entity
@Table(schema = "carts")
public class ShoppingCart implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private Long userid;

    public ShoppingCart(){}

    public int getTotalCost(){
        int sum=0;

        //// TODO: 7/8/2016 replace this with java 8 stream
        for (ShoppingCartProduct s:this.productsList){
            sum+=s.getProductPrice()*s.getQuantity();
        }
        return sum;
    }


    @OneToMany(mappedBy = "shoppingCart",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShoppingCartProduct> productsList=new ArrayList<ShoppingCartProduct>();

    public ShoppingCart(Long userid, List<ShoppingCartProduct> productsList) {
        this.userid = userid;
        this.productsList = productsList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public List<ShoppingCartProduct> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<ShoppingCartProduct> productsList) {
        this.productsList = productsList;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShoppingCart that = (ShoppingCart) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userid != null ? !userid.equals(that.userid) : that.userid != null) return false;
        return productsList != null ? productsList.equals(that.productsList) : that.productsList == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userid != null ? userid.hashCode() : 0);
        result = 31 * result + (productsList != null ? productsList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", userid=" + userid +
                ", productsList=" + productsList +
                '}';
    }
}
