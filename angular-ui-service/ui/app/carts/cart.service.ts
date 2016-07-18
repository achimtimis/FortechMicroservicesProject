import { Injectable } from 'angular2/core';
import { Http, Response } from 'angular2/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/catch';
import { ICart}  from  './cart';

@Injectable()
export class CartService {
   
    private _cartUrl = 'http://localhost:9999/api/carts';
    constructor(private _http: Http) { }

    getCartId(userId : number) : void {}

    addToCart(userId :number, productId : number, quantity : number) : void{

          this._http.get(this._cartUrl + "/user/" + userId + "/products?" + "productId=" + productId + "&quantity=" + quantity)
             .map((response: Response) => <ICart> response.json())
             .do(data => console.log('ShoppingCart Service -> Add Product: -> New Cart: ' +  JSON.stringify(data)))
             .catch(this.handleError)
             .subscribe((res) => {
                });
             location.reload();
    }
    getCartByUserId(userId : number) : Observable <ICart> {
        return this._http.get(this._cartUrl+'/user/'+userId)
            .map((response: Response) => <ICart> response.json())
            .do(data => console.log('All: ' +  JSON.stringify(data)))
    
    }

    private handleError(error: Response) {
        // in a real world app, we may send the server to some remote logging infrastructure
        // instead of just logging it to the console
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
    }


    removeFromCart(cartId : number,productId:number) : void {
        this._http.get(this._cartUrl + '/' + cartId + "/products/" + productId)
                .map((response: Response) => <ICart> response.json())
                 .do(data => console.log('ShoppingCart Service -> delete Product: -> New Cart: ' +  JSON.stringify(data)))
                 .catch(this.handleError)
                .subscribe((res) => {
                });
        location.reload();
    }

    checkout(cartId : number) : void {
        this._http.get(this._cartUrl + '/' + cartId + "/order")
                .map((response: Response) => <ICart> response.json())
                 .do(data => console.log('ShoppingCart Service -> order'))
                 .catch(this.handleError)
                .subscribe((res) => {
                });

        location.reload();
    }
    
    newCart(userId : number) : void{
        console.log("CartService  -> New Cart")
        this._http.get(this._cartUrl + "/new?userId=" + userId)
            .map((response: Response) => <ICart> response.json())
            .do(data => console.log('ShoppingCart Service -> Create New Cart: -> New Cart: ' +  JSON.stringify(data)))
            .catch(this.handleError)
            .subscribe((res) => {
            });

        location.reload();
    }

}
