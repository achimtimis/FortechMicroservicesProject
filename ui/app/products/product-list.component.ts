import { Component, OnInit }  from 'angular2/core';
import { IProduct } from './product';
import { ProductService } from './product.service';

@Component({
	selector:'pm-products',
	templateUrl: 'app/products/product-list.component.html',
})

export class ProductListComponent implements OnInit{
	pageTitle : string ='Product List';
	showImage: boolean = false;
	imageWidth : number = 50;
	imageMargin : number = 2;

     constructor(private _productService: ProductService) {

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


}