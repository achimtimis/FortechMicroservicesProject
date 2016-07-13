import { Component, OnInit }  from 'angular2/core';
import { IProduct } from './product';
import { ProductService } from './product.service';
import { ROUTER_DIRECTIVES } from 'angular2/router';
import {CartService} from './cart.service';
import { IUser} from '../users/user';
import { UserService} from '../users/user.service';
@Component({
	selector:'pm-products',
	templateUrl: 'app/products/product-list.component.html',
  directives: [ROUTER_DIRECTIVES]
})

export class ProductListComponent implements OnInit{
	pageTitle : string ='Product List';
	showImage: boolean = false;
	imageWidth : number = 50;
	imageMargin : number = 2;
  placeholder : string='';
  errorMessage : string ='';
  quantity : number = 0;
  products: IProduct[] ;
  // = [{ "id" : 1 , "username" : "aaa" , "password" : "abc"}] ;
  loggedUser: IUser;

     constructor(private _productService: ProductService,private _cartService : CartService,private _userService : UserService) {

    }

    ngOnInit(): void {
           this._productService.getProducts()
                     .subscribe(
                       products => this.products = products,
                       error =>  this.errorMessage = <any>error);
           this._userService.getUser2(1)
                     .subscribe(
                        loggedUser => this.loggedUser = loggedUser,
                        error =>  this.errorMessage = <any>error
                       );
    }


    toggleImage(): void {
        this.showImage = !this.showImage;
    }

    addToCart(productId : number,quantity : number){
      
      this.placeholder='added to cart' + ' ' +productId + ' ' + quantity ;
      alert('added to cart' + ' ' +productId + ' ' + quantity);
      // this._productService.addToCart(productId,quantity);
      this._cartService.addToCart(productId,quantity);
    }


}