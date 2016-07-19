
import { ProductListComponent } from './products/product-list.component';
import { HTTP_PROVIDERS } from '@angular/http';
import 'rxjs/Rx';  // laod all
import { ProductService } from './products/product.service';
import { ROUTER_PROVIDERS, RouteConfig, ROUTER_DIRECTIVES } from '@angular/router-deprecated';
import { WelcomeComponent } from './home/welcome.component';
import { CartService} from './carts/cart.service';
import { UserService} from './users/user.service';
import {CartComponent} from "./carts/cart.component";
import {OrderComponent} from "./order/order.component";
import {OrderService} from "./order/order.service";
import {ProductManagementComponent} from "./products/product-management.component";
import {ProductFormComponent} from "./products/product-form.component";
import {Component} from "@angular/core";
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
                    <li><a [routerLink]="['Order']">Order</a></li>
                    <li><a [routerLink]="['ProductsManagement']">Product Management</a></li>
                </ul>
            </div>
        </nav>
        <div class='container'>
            <router-outlet></router-outlet>
        </div>
     </div>
	`,
	directives: [ROUTER_DIRECTIVES],
    providers: [ProductService,CartService,UserService,OrderService,
                HTTP_PROVIDERS,
                ROUTER_PROVIDERS]
})

@RouteConfig([
    { path: '/welcome', name: 'Welcome', component: WelcomeComponent, useAsDefault: true },
    { path: '/products', name: 'Products', component: ProductListComponent },
    { path: '/products/management', name: 'ProductsManagement', component: ProductManagementComponent },
    { path: '/cart', name: 'Cart', component: CartComponent },
    { path: '/order', name: 'Order', component: OrderComponent },
    { path: '/product_form', name: 'ProductForm', component: ProductFormComponent }
    // { path: '/product/:id', name: 'ProductDetail', component: ProductDetailComponent }
])
export class AppComponent{
	pageTitle : string ='Product management';
}