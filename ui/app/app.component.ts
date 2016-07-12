import { Component } from 'angular2/core';
import { ProductListComponent } from './products/product-list.component';
import { HTTP_PROVIDERS } from 'angular2/http';
import 'rxjs/Rx';  // laod all
import { ProductService } from './products/product.service';

@Component({
	selector : 'pm-app',
	template : `
		<pm-products></pm-products>
	`,
	directives: [ProductListComponent],
    providers: [ProductService,
                HTTP_PROVIDERS]
})
export class AppComponent{
	pageTitle : string ='Product management';
}