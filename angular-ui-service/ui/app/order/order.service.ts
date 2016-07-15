/**
 * Created by Flaviu Cicio on 14.07.2016.
 */
import {Injectable} from "angular2/core";
import {Http, Response} from "angular2/http";
import {Observable} from "rxjs/Rx";
import {IOrder} from "./order";


@Injectable()
export class OrderService{

    private _orderUrl = 'http://localhost:9999/api/orders';
    
    constructor(private _http: Http) { }

    getOrderByUserId(userId: number): Observable<IOrder[]> {
        return this._http.get(this._orderUrl+'/user?id='+userId)
            .map((response: Response) => <IOrder[]> response.json())
            .do(data => console.log('OrderService -> getOrdersByUserId -> Result: ' +  JSON.stringify(data)))
        
    }
    



    private handleError(error: Response) {
        // in a real world app, we may send the server to some remote logging infrastructure
        // instead of just logging it to the console
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
    }
}