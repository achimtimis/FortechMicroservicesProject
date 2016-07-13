import { Injectable } from 'angular2/core';
import { Http, Response } from 'angular2/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/catch';
import { ICart,ICartProduct }  from  './cart';
import { IProduct } from '../products/product';

@Injectable()
export class CartService {
   
    private _cartUrl = 'http://localhost:8003/carts';
    constructor(private _http: Http) { }


    addToCart(userId :number,productId : number,quantity : number) : void {
         alert('in the cart service.trying to add to cart  of user'+userId + productId + '/'+ quantity);
         //toDo : configure the path
         // this._http.put(this._cartUrl + '/' + productId + '/'+ quantity);
    }
    getCartByUserId(userId : number) : Observable <ICart> {
        return this._http.get(this._cartUrl+'/'+userId)
            .map((response: Response) => <ICart> response.json())
            .do(data => console.log('All: ' +  JSON.stringify(data)))
    
    }

}
