import { Injectable } from 'angular2/core';
import { Http, Response } from 'angular2/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/catch';
import { CartService} from './cart.service';
import { IProduct } from './product';

@Injectable()
export class ProductService {
    // private _productUrl = 'api/products/products.json';
    private _productUrl = 'http://localhost:8002/products';
    constructor(private _http: Http) { }

    getProducts(): Observable<IProduct[]> {
        return this._http.get(this._productUrl)
            .map((response: Response) => <IProduct[]> response.json())
            .do(data => console.log('All: ' +  JSON.stringify(data)))
            .catch(this.handleError);
    }

    getProduct(id: number): Observable<IProduct> {
        return this.getProducts()
            .map((products: IProduct[]) => products.find(p => p.id === id));
    }

    private handleError(error: Response) {
        // in a real world app, we may send the server to some remote logging infrastructure
        // instead of just logging it to the console
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
    }

    // will not use this
    addToCart(productId : number,quantity : number) : void {
         alert('in the service.trying to add to cart ' + productId + '/'+ quantity);
         // this._http.put(this._productUrl + '/' + productId + '/'+ quantity);
    }
}
