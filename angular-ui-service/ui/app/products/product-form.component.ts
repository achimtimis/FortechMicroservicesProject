/**
 * Created by Flaviu Cicio on 18.07.2016.
 */
import {Product, IProduct} from "./product";
import {ProductCrudService} from "./product-crud.service";
import {OnInit, Component, Input} from "@angular/core";
@Component({
    selector: 'product-form',
    templateUrl: 'app/products/product-form.component.html'
})
export class ProductFormComponent implements OnInit{
    ngOnInit():any {
        return undefined;
    }

    constructor(private _productCrudService:ProductCrudService) {
       this.model = new Product(null, '', 0, 0);
    }
    
    @Input()
    model:IProduct = new Product(null, '', 0, 0);
    
    submitted = false;
    onSubmit() {
        this._productCrudService.addProduct(this.model);
        this.submitted = true;
        location.reload();
        console.log("Product Form Submitted");
    }

    // TODO: Remove this when we're done
    get diagnostic() { return JSON.stringify(this.model); }

    editMode():boolean{

        if(this.model.id != null){
            return true;
        }
        return false;
    }
}
