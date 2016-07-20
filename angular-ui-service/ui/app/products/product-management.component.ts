import {OnInit, Component} from "@angular/core";
import {ProductService} from "./product.service";
import {IProduct, Product} from "./product";
import {ROUTER_DIRECTIVES} from "@angular/router-deprecated";
import {ProductCrudService} from "./product-crud.service";
import {ProductFormComponent} from "./product-form.component";

/**
 * Created by Flaviu Cicio on 15.07.2016.
 */

@Component({
    selector: 'products-management',
    templateUrl: 'app/products/product-management.component.html',
    directives: [ROUTER_DIRECTIVES, ProductFormComponent],
    providers: [ProductCrudService, ProductFormComponent]
})
export class ProductManagementComponent implements OnInit {

    products: IProduct[];
    errorMessage:string = '';
    pageTitle:string = 'Product Management';

    model:IProduct = new Product(null, "", -1, -1);
    
    constructor(private _productCrudService:ProductCrudService) {
    }

    ngOnInit():any {
        this._productCrudService.getAllProductsInfo()
            .subscribe(
                products => this.products = products,
                error => this.errorMessage = <any>error);
    }
    
    deleteProduct(id: number){
        console.log("ProductManagement -> DeleteProduct with id: " + id);
        this._productCrudService.removeProduct(id);
        location.reload();
        location.reload();

    }
    
    setEditProduct(id: number, name: string, stock:number, price:number){
        this.model = new Product(id, name, stock, price);
    }

    unsetEditProduct(){
        this.model = new Product(null, '', -1, -1);
    }

    editMode():boolean{
        if(this.model.id == null){
            return false;
        }
        return true;
    }
    
    getModelId(): number{
        return this.model.id;
    }
    

}