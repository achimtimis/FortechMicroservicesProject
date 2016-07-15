System.register(['angular2/core', './product.service', 'angular2/router', '../carts/cart.service', '../users/user.service', '../carts/cart.component'], function(exports_1, context_1) {
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
    var core_1, product_service_1, router_1, cart_service_1, user_service_1, cart_component_1;
    var ProductListComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (product_service_1_1) {
                product_service_1 = product_service_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (cart_service_1_1) {
                cart_service_1 = cart_service_1_1;
            },
            function (user_service_1_1) {
                user_service_1 = user_service_1_1;
            },
            function (cart_component_1_1) {
                cart_component_1 = cart_component_1_1;
            }],
        execute: function() {
            ProductListComponent = (function () {
                function ProductListComponent(_productService, _cartService, _userService) {
                    this._productService = _productService;
                    this._cartService = _cartService;
                    this._userService = _userService;
                    this.pageTitle = 'Product List';
                    this.placeholder = '';
                    this.errorMessage = '';
                    this.stockMessage = '';
                    this.quantity = 0;
                    this.userID = 1;
                }
                ProductListComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    this._productService.getProducts()
                        .subscribe(function (products) { return _this.products = products; }, function (error) { return _this.errorMessage = error; });
                    this._userService.getUser2(this.userID)
                        .subscribe(function (loggedUser) { return _this.loggedUser = loggedUser; }, function (error) { return _this.errorMessage = error; });
                    this._cartService.getCartByUserId(this._userService.getUserID())
                        .subscribe(function (cart) { return _this.cart = cart; }, function (error) { return _this.errorMessage = error; });
                };
                ProductListComponent.prototype.addToCart = function (userId, productId, quantity, stock) {
                    if (stock < quantity || stock <= 0) {
                        this.stockMessage = 'Not enough products on stock!';
                    }
                    else {
                        this.stockMessage = '';
                        this.placeholder = 'added to cart  ' + ' user id:' + userId + ' product id ' + productId + ' quantity ' + quantity;
                        // alert('added to cart' + ' ' +productId + ' ' + quantity);
                        // this._productService.addToCart(productId,quantity);
                        this._cartService.addToCart(userId, productId, quantity);
                    }
                };
                ProductListComponent.prototype.cartExists = function () {
                    return this.cart == null;
                };
                ProductListComponent = __decorate([
                    core_1.Component({
                        selector: 'pm-products',
                        templateUrl: 'app/products/product-list.component.html',
                        directives: [router_1.ROUTER_DIRECTIVES, cart_component_1.CartComponent],
                        providers: [cart_component_1.CartComponent]
                    }), 
                    __metadata('design:paramtypes', [product_service_1.ProductService, cart_service_1.CartService, user_service_1.UserService])
                ], ProductListComponent);
                return ProductListComponent;
            }());
            exports_1("ProductListComponent", ProductListComponent);
        }
    }
});
//# sourceMappingURL=product-list.component.js.map