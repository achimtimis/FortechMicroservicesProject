import {Http, Response} from "angular2/http";
import {IProduct} from "./product";
import {Observable} from "rxjs/Rx";
import {Injectable} from "angular2/core";

@Injectable()
export class CRUDProductService{

    private _productUrl = 'http://localhost:9999/api/products';

    constructor(private _http: Http) {
    }


    getAllEmployeeInfo() : Observable<IProduct[]>{
    return this._http.get(this._productUrl)
        .map((response: Response) => <IProduct[]> response.json())
        .do(data => console.log('CRUDProductService -> getAllProducts -> Result: ' +  JSON.stringify(data)))
};

    addProduct(product : IProduct) {
    var ServerData = this._http.get(this._productUrl,{
        method: "Get",
        url: this._productUrl,
        data: JSON.stringify(product),
        dataType: 'json',
        contentType: 'application/json',
    });
    return ServerData;
}

//     this.EditEmployee = function (EmployeeID) {
//     var ServerData = $http({
//         method: "Post",
//         url: "/Home/GetEmployeeById",
//         data: JSON.stringify({ EmpID: EmployeeID }),
//         dataType: 'json',
//         //contentType: 'application/json',
//     });
//     return ServerData;
// }
//
//     this.UpdateEmployee = function (EmployeeViewModel) {
//     var ServerData = $http({
//         method: "Post",
//         url: "/Home/UpdateEmployee",
//         data: JSON.stringify({ EmployeeViewModelClient: EmployeeViewModel }),
//         dataType: 'json',
//         //contentType: 'application/json',
//     });
//     return ServerData;
// }
//
//     this.DeleteEmployee = function (EmployeeID) {
//     var ServerData = $http({
//         method: "Post",
//         url: "/Home/DeleteByID",
//         data: JSON.stringify({ EmpID: EmployeeID }),
//         dataType: 'json',
//         //contentType: 'application/json',
//     });
//     return ServerData;
// }

}