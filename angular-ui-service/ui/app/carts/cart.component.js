System.register(['angular2/core', 'angular2/router', './cart.service', '../users/user.service'], function(exports_1, context_1) {
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
    var core_1, router_1, cart_service_1, user_service_1;
    var CartComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (cart_service_1_1) {
                cart_service_1 = cart_service_1_1;
            },
            function (user_service_1_1) {
                user_service_1 = user_service_1_1;
            }],
        execute: function() {
            CartComponent = (function () {
                function CartComponent(_cartService, _userService) {
                    this._cartService = _cartService;
                    this._userService = _userService;
                    this.pageTitle = 'Cart Items';
                    this.placeholder = '';
                    this.size = 0;
                    this.totalCost = 123;
                }
                CartComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    this._cartService.getCartByUserId(this._userService.getUserID())
                        .subscribe(function (cart) { return _this.cart = cart; }, function (error) { return _this.errorMessage = error; });
                    this.loggedUserId = this._userService.getUserID();
                    console.log("Logged in user id: " + this.loggedUserId);
                };
                CartComponent.prototype.addToCart = function (userId, productId, quantity) {
                    this.placeholder = 'added to cart' + ' user id:' + userId + ' product id ' + productId + ' quantity ' + quantity;
                    // this._productService.addToCart(productId,quantity);
                    this._cartService.addToCart(userId, productId, quantity);
                    // location.reload();
                };
                CartComponent.prototype.removeFromCart = function (cartId, productId) {
                    this._cartService.removeFromCart(cartId, productId);
                    location.reload();
                };
                CartComponent.prototype.checkout = function (cartId) {
                    this._cartService.checkout(cartId);
                };
                CartComponent.prototype.newCart = function () {
                    console.log("CartComponent -> New Cart");
                    this._cartService.newCart(this.loggedUserId);
                };
                CartComponent.prototype.cartExists = function () {
                    if (this.cart != null) {
                        return true;
                    }
                    return false;
                };
                CartComponent.prototype.cartHasProducts = function () {
                    if (this.cart.productsList.length > 0) {
                        return true;
                    }
                    else
                        return false;
                };
                CartComponent = __decorate([
                    core_1.Component({
                        selector: 'pm-carts',
                        templateUrl: 'app/carts/cart.component.html',
                        directives: [router_1.ROUTER_DIRECTIVES]
                    }), 
                    __metadata('design:paramtypes', [cart_service_1.CartService, user_service_1.UserService])
                ], CartComponent);
                return CartComponent;
            }());
            exports_1("CartComponent", CartComponent);
        }
    }
});
//# sourceMappingURL=cart.component.js.map