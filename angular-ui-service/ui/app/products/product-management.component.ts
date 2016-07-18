import {OnInit, Component} from "angular2/core";
import {ProductService} from "./product.service";
import {IProduct} from "./product";
import {ROUTER_DIRECTIVES} from "angular2/router";

/**
 * Created by Flaviu Cicio on 15.07.2016.
 */

@Component({
    selector: 'products-management',
    templateUrl: 'app/products/product-management.component.html',
    directives: [ROUTER_DIRECTIVES]
})
export class ProductManagementComponent implements OnInit {

    products: IProduct[];
    errorMessage:string = '';
    pageTitle:string = 'Product Management'

    ProductDetailsModel = {
      prodId: '',
        prodName: '',
        prodStock: '',
        prodPrice: ''
    };
    
    constructor(private _productService:ProductService) {
    }

    ngOnInit():any {
        this._productService.getProducts()
            .subscribe(
                products => this.products = products,
                error => this.errorMessage = <any>error);
    }


//     $scope.EditProduct = function (ProductId) {
//     var EditedProduct = CRUDService.EditEmployee(EmployeeID);
//     EditedEmployee.then(function (Emp) {
//         $scope.EmpDetailsModel.EmpID = Emp.data[0].empDetailModel.EmpID;
//         $scope.EmpDetailsModel.EmpName = Emp.data[0].empDetailModel.EmpName;
//         $scope.EmpDetailsModel.EmpPhone = Emp.data[0].empDetailModel.EmpPhone;
//         $scope.EmpAddressModel.Address1 = Emp.data[0].empAddressModel.Address1
//         $scope.EmpAddressModel.Address2 = Emp.data[0].empAddressModel.Address2;
//         $scope.EmpAddressModel.Address3 = Emp.data[0].empAddressModel.Address3;
//         $scope.$apply();
//     })
// };

}