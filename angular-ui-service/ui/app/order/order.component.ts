/**
 * Created by Flaviu Cicio on 14.07.2016.
 */

import {ROUTER_DIRECTIVES} from "angular2/router";
import {Component, OnInit} from "angular2/core";
import {IOrder} from "./order";
import {OrderService} from "./order.service";
import {UserService} from "../users/user.service";


@Component({
    selector:'pm-orders',
    templateUrl: 'app/order/order.component.html',
    directives: [ROUTER_DIRECTIVES]
})

export class OrderComponent implements OnInit{
    pageTitle : string ='Your Orders';
    placeholder : string='';
    orders : IOrder[];
    errorMessage :string;

    constructor(private _orderService : OrderService, private _userService : UserService){}

    ngOnInit(): void {

        this._orderService.getOrderByUserId(1)
            .subscribe(
                orders => this.orders = orders,
                error =>  this.errorMessage = <any>error);


    }
    
}

