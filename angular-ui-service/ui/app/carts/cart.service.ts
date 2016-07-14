import { Injectable } from 'angular2/core';
import { Http, Response } from 'angular2/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/catch';
import { ICart,ICartProduct }  from  './cart';
import { IProduct } from '../products/product';

@Injectable()
export class CartService {
   
    private _cartUrl = 'http://localhost:9999/api/carts';
    constructor(private _http: Http) { }


    addToCart(cartId:number, userId :number, productId : number, quantity : number) : Observable<ICart> {
         return this._http.put(this._cartUrl + '/' + cartId + "/products?" + "productId=" + productId + "&quantity=" + quantity, null).map((response: Response) => <IProduct> response.json())
             .do(data => console.log('ShoppingCart Service -> Add Product: -> New Cart: ' +  JSON.stringify(data)))
             .catch(this.handleError);;
    }
    getCartByUserId(userId : number) : Observable <ICart> {
        return this._http.get(this._cartUrl+'/'+userId)
            .map((response: Response) => <ICart> response.json())
            .do(data => console.log('All: ' +  JSON.stringify(data)))
    
    }

    private handleError(error: Response) {
        // in a real world app, we may send the server to some remote logging infrastructure
        // instead of just logging it to the console
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
    }

}
