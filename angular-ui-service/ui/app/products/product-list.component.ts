import {Component, OnInit}  from 'angular2/core';
import {IProduct} from './product';
import {ProductService} from './product.service';
import {ROUTER_DIRECTIVES} from 'angular2/router';
import {CartService} from '../carts/cart.service';
import {IUser} from '../users/user';
import {UserService} from '../users/user.service';
import {CartComponent} from '../carts/cart.component';
import {ICart} from "../carts/cart";
@Component({
    selector: 'pm-products',
    templateUrl: 'app/products/product-list.component.html',
    directives: [ROUTER_DIRECTIVES, CartComponent],
    providers: [CartComponent]
})

export class ProductListComponent implements OnInit {
    pageTitle:string = 'Product List';
    placeholder:string = '';
    errorMessage:string = '';
    stockMessage:string = '';
    quantity:number = 0;
    products:IProduct[];
    loggedUser:IUser;
    userID:number = 1;
    cart:ICart;


    constructor(private _productService:ProductService, private _cartService:CartService, private _userService:UserService) {

    }

    ngOnInit():void {
        this._productService.getProducts()
            .subscribe(
                products => this.products = products,
                error => this.errorMessage = <any>error);
        this._userService.getUser2(this.userID)
            .subscribe(
                loggedUser => this.loggedUser = loggedUser,
                error => this.errorMessage = <any>error
            );

        this._cartService.getCartByUserId(this._userService.getUserID())
            .subscribe(
                cart => this.cart = cart,
                error => this.errorMessage = <any>error);

    }


    addToCart(userId:number, productId:number, quantity:number, stock:number) {
        if (stock < quantity || stock <= 0) {
            this.stockMessage = 'Not enough products on stock!';

        }

        else {
            this.stockMessage = '';
            this.placeholder = 'added to cart  ' + ' user id:' + userId + ' product id ' + productId + ' quantity ' + quantity;
            // alert('added to cart' + ' ' +productId + ' ' + quantity);
            // this._productService.addToCart(productId,quantity);
            this._cartService.addToCart(userId, productId, quantity);
        }
    }

    cartExists():void {
        return this.cart==null;
    }

}