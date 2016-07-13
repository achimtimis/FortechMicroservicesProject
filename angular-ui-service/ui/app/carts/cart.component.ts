import { Component, OnInit }  from 'angular2/core';
import { IProduct } from '../products/product';
import { ProductService } from '../products/product.service';
import { ROUTER_DIRECTIVES } from 'angular2/router';
import { CartService } from './cart.service';
import { IUser } from '../users/user';
import { UserService } from '../users/user.service';
import { ICart,ICartProduct} from './cart';

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
  loggedUser: IUser;
  size : number = 0;
  totalCost  : number = 123;
     constructor(private _cartService : CartService,private _userService : UserService) {

    }

    ngOnInit(): void {

            this._cartService.getCartByUserId(this._userService.getUserID())
                       .subscribe(
                       cart => this.cart = cart,
                       error =>  this.errorMessage = <any>error);
           
    }


    

    addToCart(userId : number,productId : number,quantity : number){
      
      this.placeholder='added to cart' + ' user id:' +userId +' product id '+productId + ' quantity ' + quantity ;
      // alert('added to cart' + ' ' +productId + ' ' + quantity);
      // this._productService.addToCart(productId,quantity);
      this._cartService.addToCart(userId,productId,quantity);
    }
    removeFromCart(cartId : number,productId : number){
        alert("will remove product with id " + productId +" from cart with id " + cartId);  
    }


}