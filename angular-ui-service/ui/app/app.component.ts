import { Component } from 'angular2/core';
import { ProductListComponent } from './products/product-list.component';
import { HTTP_PROVIDERS } from 'angular2/http';
import 'rxjs/Rx';  // laod all
import { ProductService } from './products/product.service';
import { ROUTER_PROVIDERS, RouteConfig, ROUTER_DIRECTIVES } from 'angular2/router';
import { WelcomeComponent } from './home/welcome.component';
import { CartService} from './carts/cart.service';
import { UserService} from './users/user.service';
import {CartComponent} from "./carts/cart.component";
@Component({
	selector : 'pm-app',
	template : `
		<div>
        <nav class='navbar navbar-default'>
            <div class='container-fluid'>
                <a class='navbar-brand'>{{pageTitle}}</a>
                <ul class='nav navbar-nav'>
                    <li><a [routerLink]="['Welcome']">Home</a></li>
                    <li><a [routerLink]="['Products']">Product List</a></li>
                </ul>
            </div>
        </nav>
        <div class='container'>
            <router-outlet></router-outlet>
        </div>
     </div>
	`,
	directives: [ROUTER_DIRECTIVES],
    providers: [ProductService,CartService,UserService,
                HTTP_PROVIDERS,
                ROUTER_PROVIDERS]
})

@RouteConfig([
    { path: '/welcome', name: 'Welcome', component: WelcomeComponent, useAsDefault: true },
    { path: '/products', name: 'Products', component: ProductListComponent },
    { path: '/cart', name: 'Cart', component: CartComponent }
    // { path: '/product/:id', name: 'ProductDetail', component: ProductDetailComponent }
])
export class AppComponent{
	pageTitle : string ='Product management';
}