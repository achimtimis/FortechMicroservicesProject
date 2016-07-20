System.register(["@angular/http", "@angular/core"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var http_1, core_1;
    var ProductCrudService;
    return {
        setters:[
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (core_1_1) {
                core_1 = core_1_1;
            }],
        execute: function() {
            ProductCrudService = (function () {
                function ProductCrudService(_http) {
                    this._http = _http;
                    this._productUrl = 'http://localhost:9999/api/products';
                    this.headers = new http_1.Headers();
                    this.headers.append('Content-Type', 'application/json');
                    this.headers.append('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
                    this.headers.append('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
                }
                ProductCrudService.prototype.getAllProductsInfo = function () {
                    return this._http.get(this._productUrl)
                        .map(function (response) { return response.json(); })
                        .do(function (data) { return console.log('CRUDProductService -> getAllProducts -> Result: ' + JSON.stringify(data)); });
                };
                ;
                ProductCrudService.prototype.addProduct = function (product) {
                    var _this = this;
                    var ServerData = this._http.post(this._productUrl, JSON.stringify(product), {
                        headers: this.headers
                    }).subscribe(null, function (err) { return _this.err; });
                    console.log(this.err);
                    return ServerData;
                };
                ProductCrudService.prototype.removeProduct = function (id) {
                    var _this = this;
                    this._http.delete(this._productUrl + "?id=" + id, {
                        headers: this.headers
                    }).subscribe(null, function (err) { return _this.err; });
                    console.log(this.err);
                };
                ProductCrudService = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [http_1.Http])
                ], ProductCrudService);
                return ProductCrudService;
            }());
            exports_1("ProductCrudService", ProductCrudService);
        }
    }
});
//# sourceMappingURL=product-crud.service.js.map