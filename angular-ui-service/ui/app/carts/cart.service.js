System.register(['@angular/core', '@angular/http', 'rxjs/Observable', 'rxjs/add/operator/do', 'rxjs/add/operator/catch'], function(exports_1, context_1) {
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
    var core_1, http_1, Observable_1;
    var CartService;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (_1) {},
            function (_2) {}],
        execute: function() {
            CartService = (function () {
                function CartService(_http) {
                    this._http = _http;
                    this._cartUrl = 'http://localhost:9999/api/carts';
                }
                CartService.prototype.getCartId = function (userId) { };
                CartService.prototype.addToCart = function (userId, productId, quantity) {
                    this._http.get(this._cartUrl + "/user/" + userId + "/products?" + "productId=" + productId + "&quantity=" + quantity)
                        .map(function (response) { return response.json(); })
                        .do(function (data) { return console.log('ShoppingCart Service -> Add Product: -> New Cart: ' + JSON.stringify(data)); })
                        .catch(this.handleError)
                        .subscribe(function (res) {
                    });
                    location.reload();
                };
                CartService.prototype.getCartByUserId = function (userId) {
                    return this._http.get(this._cartUrl + '/user/' + userId)
                        .map(function (response) { return response.json(); })
                        .do(function (data) { return console.log('All: ' + JSON.stringify(data)); });
                };
                CartService.prototype.handleError = function (error) {
                    // in a real world app, we may send the server to some remote logging infrastructure
                    // instead of just logging it to the console
                    console.error(error);
                    return Observable_1.Observable.throw(error.json().error || 'Server error');
                };
                CartService.prototype.removeFromCart = function (cartId, productId) {
                    this._http.get(this._cartUrl + '/' + cartId + "/products/" + productId)
                        .map(function (response) { return response.json(); })
                        .do(function (data) { return console.log('ShoppingCart Service -> delete Product: -> New Cart: ' + JSON.stringify(data)); })
                        .catch(this.handleError)
                        .subscribe(function (res) {
                    });
                    location.reload();
                };
                CartService.prototype.checkout = function (cartId) {
                    this._http.get(this._cartUrl + '/' + cartId + "/order")
                        .map(function (response) { return response.json(); })
                        .do(function (data) { return console.log('ShoppingCart Service -> order'); })
                        .catch(this.handleError)
                        .subscribe(function (res) {
                    });
                    location.reload();
                };
                CartService.prototype.newCart = function (userId) {
                    console.log("CartService  -> New Cart");
                    this._http.get(this._cartUrl + "/new?userId=" + userId)
                        .map(function (response) { return response.json(); })
                        .do(function (data) { return console.log('ShoppingCart Service -> Create New Cart: -> New Cart: ' + JSON.stringify(data)); })
                        .catch(this.handleError)
                        .subscribe(function (res) {
                    });
                    location.reload();
                };
                CartService = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [http_1.Http])
                ], CartService);
                return CartService;
            }());
            exports_1("CartService", CartService);
        }
    }
});
//# sourceMappingURL=cart.service.js.map