import { Injectable } from 'angular2/core';
import { Http, Response } from 'angular2/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/catch';

import { IProduct } from './product';

@Injectable()
export class CartService {
   
    private _cartUrl = 'http://localhost:8003/carts';
    constructor(private _http: Http) { }


    addToCart(productId : number,quantity : number) : void {
         alert('in the cart service.trying to add to cart ' + productId + '/'+ quantity);
         //toDo : configure the path
         // this._http.put(this._cartUrl + '/' + productId + '/'+ quantity);
    }
}
