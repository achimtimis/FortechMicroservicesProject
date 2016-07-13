import { Component, OnInit }  from 'angular2/core';
import { IProduct } from '../products/product';
import { ProductService } from '../products/product.service';
import { ROUTER_DIRECTIVES } from 'angular2/router';
import { CartService } from './cart.service';
import { IUser } from '../users/user';
import { UserService } from '../users/user.service';

@Component({
	selector:'pm-carts',
	templateUrl: 'app/carts/cart.component.html',
  directives: [ROUTER_DIRECTIVES]
})

export class CartComponent implements OnInit{
	pageTitle : string ='Cart Items';
  placeholder : string='';
  products: IProduct[] ;
  loggedUser: IUser;
  size : number = 0;
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

    addToCart(userId : number,productId : number,quantity : number){
      
      this.placeholder='added to cart' + ' user id:' +userId +' product id '+productId + ' quantity ' + quantity ;
      // alert('added to cart' + ' ' +productId + ' ' + quantity);
      // this._productService.addToCart(productId,quantity);
      this._cartService.addToCart(userId,productId,quantity);
    }


}