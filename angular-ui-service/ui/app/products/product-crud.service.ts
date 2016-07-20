import {Http, Response, Headers} from "@angular/http";
import {IProduct} from "./product";
import {Observable} from "rxjs/Rx";
import {Injectable} from "@angular/core";

@Injectable()
export class ProductCrudService{

    private _productUrl = 'http://localhost:9999/api/products';
    private err: string;
    private headers:Headers;

    constructor(private _http: Http) {
        this.headers = new Headers();
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
        this.headers.append('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');

    }


    getAllProductsInfo() : Observable<IProduct[]>{
    return this._http.get(this._productUrl)
        .map((response: Response) => <IProduct[]> response.json())
        .do(data => console.log('CRUDProductService -> getAllProducts -> Result: ' +  JSON.stringify(data)))
};

    addProduct(product : IProduct) {
        var ServerData = this._http.post(this._productUrl, JSON.stringify(product), {
            headers: this.headers
        }).subscribe(null, err => this.err);
        console.log(this.err);
        return ServerData;
}

    removeProduct(id : number){
        this._http.delete(this._productUrl + "?id=" + id, {
            headers: this.headers
        }).subscribe(null, err => this.err);
        console.log(this.err);
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