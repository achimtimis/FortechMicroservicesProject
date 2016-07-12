import { Component, OnInit }  from 'angular2/core';
import { IProduct } from './product';
import { ProductService } from './product.service';
import { ROUTER_DIRECTIVES } from 'angular2/router';
import {CartService} from './cart.service';
@Component({
	selector:'pm-products',
	templateUrl: 'app/products/product-list.component.html',
  directives: [ROUTER_DIRECTIVES]
})

export class ProductListComponent implements OnInit{
	pageTitle : string ='Product List';
	showImage: boolean = false;
	imageWidth : number = 50;
	imageMargin : number = 2;
  placeholder : string='';
  quantity : number = 0;
     constructor(private _productService: ProductService,private _cartService : CartService) {

    }

    ngOnInit(): void {
           this._productService.getProducts()
                     .subscribe(
                       products => this.products = products,
                       error =>  this.errorMessage = <any>error);
    }

	products: IProduct[] ;
 //    {
 //        "id": 1,
 //        "name": "Leaf Rake",
 //        "stock": 13,
 //        "price": 16
 //    },
 //    {
 //        "id": 2,
 //        "name": "Leaf Rake",
 //        "stock": 14,
 //        "price": 16
 //    }];

    toggleImage(): void {
        this.showImage = !this.showImage;
    }
    addToCart(productId : number,quantity : number){
      
      this.placeholder='added to cart' + ' ' +productId + ' ' + quantity ;
      alert('added to cart' + ' ' +productId + ' ' + quantity);
      // this._productService.addToCart(productId,quantity);
      this._cartService.addToCart(productId,quantity);
    }


}