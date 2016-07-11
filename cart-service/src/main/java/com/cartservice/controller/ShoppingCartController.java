package com.cartservice.controller;

import com.cartservice.repository.ShoppingCartProductsRepository;
import com.cartservice.repository.ShoppingCartRepository;
import com.shopcommon.model.Product;
import com.shopcommon.model.ShoppingCart;
import com.shopcommon.model.ShoppingCartProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Achim Timis on 7/7/2016.
 */

@RestController
public class ShoppingCartController {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    ShoppingCartProductsRepository shoppingCartProductsRepository;

    @RequestMapping(value = "carts",method= RequestMethod.GET)
    public List<ShoppingCart> getShoppingCarts(){
        return this.shoppingCartRepository.findAll();

    }

    @RequestMapping(value = "carts/{id}",method=RequestMethod.GET)
    public ShoppingCart getShoppinngCart(@PathVariable  Long id ) {
        ShoppingCart shoppingCart= this.shoppingCartRepository.findOne(id);
        shoppingCart.getProductsList();
        return shoppingCart;
    }

    @RequestMapping(value = "carts/{id}", method = RequestMethod.DELETE)
    public ShoppingCart deleteShoppingCart(@PathVariable Long id){
        ShoppingCart shoppingCart= shoppingCartRepository.findOne(id);
        if (shoppingCart!=null){


        List<ShoppingCartProduct> products = shoppingCart.getProductsList();
        products.stream().forEach(p -> shoppingCartProductsRepository.delete(p));
        shoppingCartRepository.delete(id);
        return shoppingCart;}
        return null;

    }
    @RequestMapping(value = "carts/{id}", method = RequestMethod.PUT)
    public ShoppingCart updateShoppingCart(@PathVariable Long id,@RequestBody ShoppingCart shoppingCart){
        ShoppingCart shoppingCartfound=shoppingCartRepository.findOne(id);
        if (shoppingCartfound!=null){
            List<ShoppingCartProduct> shoppingCartProducts =shoppingCart.getProductsList();
            shoppingCartProducts.stream().forEach(p->p.setShoppingCart(shoppingCart));
            shoppingCartfound.setProductsList(shoppingCartProducts);
            shoppingCartfound.setId(id);
            shoppingCartfound.setUserid(shoppingCart.getUserid());
            return shoppingCartRepository.save(shoppingCartfound);
        }
        return null;
    }
    @RequestMapping(value = "carts", method = RequestMethod.POST)
    public ShoppingCart create(@RequestBody ShoppingCart shoppingCart){
        List<ShoppingCartProduct> shoppingCartProducts=shoppingCart.getProductsList();
        shoppingCartProducts.stream().forEach(p->p.setShoppingCart(shoppingCart));
        shoppingCart.setProductsList(shoppingCartProducts);
        return shoppingCartRepository.saveAndFlush(shoppingCart);
}

    @RequestMapping(value = "cart/{id}",method=RequestMethod.GET)
    public List<ShoppingCart> findShoppingCartByUserid(@PathVariable Long id){
        return this.shoppingCartRepository.findByUserid(id);
    }

    @RequestMapping(value = "cart/{id}/{quantity}",method=RequestMethod.POST)
    public ShoppingCart addProducttoShoppingCart(@PathVariable Long id,@PathVariable int quantity,@RequestBody Product product){
        if (product.getStock() >= quantity){
        ShoppingCart shoppingCart=this.shoppingCartRepository.findOne(id);
//        Is quantity used right?
        ShoppingCartProduct shoppingCartProduct= new ShoppingCartProduct(product.getId(),quantity,product.getName(),product.getPrice());
        shoppingCartProduct.setShoppingCart(shoppingCart);
        shoppingCart.getProductsList().add(shoppingCartProduct);
        //// TODO: 7/8/2016 communicate with the product service to update the stock
        shoppingCartProductsRepository.save(shoppingCartProduct);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
        }
        else return null;
    }


    // TODO: 7/8/2016  parse all carts and update/delete the given product in them
    public  void updateProduct(Product p){}
}
