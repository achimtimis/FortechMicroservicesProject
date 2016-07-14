import { Component, OnInit }  from 'angular2/core';
import { IProduct } from './product';
import { ProductService } from './product.service';
import { ROUTER_DIRECTIVES } from 'angular2/router';
import {CartService} from '../carts/cart.service';
import { IUser} from '../users/user';
import { UserService} from '../users/user.service';
import {CartComponent} from '../carts/cart.component';
@Component({
	selector:'pm-products',
	templateUrl: 'app/products/product-list.component.html',
  directives: [ROUTER_DIRECTIVES,CartComponent]
})

export class ProductListComponent implements OnInit{
	pageTitle : string ='Product List';
  placeholder : string='';
  errorMessage : string ='';
  quantity : number = 0;
  products: IProduct[] ;
  loggedUser: IUser;
  userID : number = 1;


     constructor(private _productService: ProductService,private _cartService : CartService,private _userService : UserService) {

    }

    ngOnInit(): void {
           this._productService.getProducts()
                     .subscribe(
                       products => this.products = products,
                       error =>  this.errorMessage = <any>error);
           this._userService.getUser2(this.userID)
                     .subscribe(
                        loggedUser => this.loggedUser = loggedUser,
                        error =>  this.errorMessage = <any>error
                       );
           
    }


    

    addToCart(userId : number,productId : number,quantity : number,stock : number){
      if (stock < quantity || stock <= 0){
        this.errorMessage='Quantity exceedes the stock or the item is sold out';
        
      }
      else{
        this.errorMessage='';
        this.placeholder='added to cart  '  + ' user id:' +userId +' product id '+productId + ' quantity ' + quantity ;
      // alert('added to cart' + ' ' +productId + ' ' + quantity);
      // this._productService.addToCart(productId,quantity);
        this._cartService.addToCart(userId,productId,quantity);
      }
      
    }


}