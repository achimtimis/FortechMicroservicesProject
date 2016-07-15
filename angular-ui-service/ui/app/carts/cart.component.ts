import { Component, OnInit }  from 'angular2/core';
import { ROUTER_DIRECTIVES } from 'angular2/router';
import { CartService } from './cart.service';
import { IUser } from '../users/user';
import { UserService } from '../users/user.service';
import { ICart,ICartProduct} from './cart';
import {$r} from "angular2/src/compiler/chars";

@Component({
	selector:'pm-carts',
	templateUrl: 'app/carts/cart.component.html',
  directives: [ROUTER_DIRECTIVES]
})

export class CartComponent implements OnInit{
	pageTitle : string ='Cart Items';
  placeholder : string='';
  cart: ICart;
  errorMessage :string;
  loggedUserId: number;
  size : number = 0;
  deletedCart : ICart;
  totalCost  : number = 123;

     constructor(private _cartService : CartService,private _userService : UserService) {

    }

    ngOnInit(): void {

            this._cartService.getCartByUserId(this._userService.getUserID())
                       .subscribe(
                       cart => this.cart = cart,
                       error =>  this.errorMessage = <any>error);
            this.loggedUserId = this._userService.getUserID();
            console.log("Logged in user id: " + this.loggedUserId);
    }


    

    addToCart(userId : number,productId : number,quantity : number){
      
      this.placeholder='added to cart' + ' user id:' +userId +' product id '+productId + ' quantity ' + quantity ;
      // this._productService.addToCart(productId,quantity);
      this._cartService.addToCart(userId,productId,quantity);

      // location.reload();
    }
    removeFromCart(cartId : number,productId : number){
        this._cartService.removeFromCart(cartId,productId);
        location.reload();
    }
    checkout (cartId : number) : void {
       this._cartService.checkout(cartId);
    }
    
    newCart(){
        console.log("CartComponent -> New Cart")
        this._cartService.newCart(this.loggedUserId);
    }

    cartExists() : boolean{
        if(this.cart != null){
            return true;
        }

        return false;
    }
    
    cartHasProducts() : boolean{
        if(this.cart.productsList.length > 0){
            return true;
        }
        else return false;
    }

}