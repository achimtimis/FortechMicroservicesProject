System.register(['angular2/core', './products/product-list.component', 'angular2/http', 'rxjs/Rx', './products/product.service', 'angular2/router', './home/welcome.component', './carts/cart.service', './users/user.service', "./carts/cart.component", "./order/order.component", "./order/order.service", "./products/product-management.component"], function(exports_1, context_1) {
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
    var core_1, product_list_component_1, http_1, product_service_1, router_1, welcome_component_1, cart_service_1, user_service_1, cart_component_1, order_component_1, order_service_1, product_management_component_1;
    var AppComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (product_list_component_1_1) {
                product_list_component_1 = product_list_component_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (_1) {},
            function (product_service_1_1) {
                product_service_1 = product_service_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (welcome_component_1_1) {
                welcome_component_1 = welcome_component_1_1;
            },
            function (cart_service_1_1) {
                cart_service_1 = cart_service_1_1;
            },
            function (user_service_1_1) {
                user_service_1 = user_service_1_1;
            },
            function (cart_component_1_1) {
                cart_component_1 = cart_component_1_1;
            },
            function (order_component_1_1) {
                order_component_1 = order_component_1_1;
            },
            function (order_service_1_1) {
                order_service_1 = order_service_1_1;
            },
            function (product_management_component_1_1) {
                product_management_component_1 = product_management_component_1_1;
            }],
        execute: function() {
            AppComponent = (function () {
                function AppComponent() {
                    this.pageTitle = 'Product management';
                }
                AppComponent = __decorate([
                    core_1.Component({
                        selector: 'pm-app',
                        template: "\n\t\t<div>\n        <nav class='navbar navbar-default'>\n            <div class='container-fluid'>\n                <a class='navbar-brand'>{{pageTitle}}</a>\n                <ul class='nav navbar-nav'>\n                    <li><a [routerLink]=\"['Welcome']\">Home</a></li>\n                    <li><a [routerLink]=\"['Products']\">Product List</a></li>\n                    <li><a [routerLink]=\"['Order']\">Order</a></li>\n                    <li><a [routerLink]=\"['ProductsManagement']\">Product Management</a></li>\n                </ul>\n            </div>\n        </nav>\n        <div class='container'>\n            <router-outlet></router-outlet>\n        </div>\n     </div>\n\t",
                        directives: [router_1.ROUTER_DIRECTIVES],
                        providers: [product_service_1.ProductService, cart_service_1.CartService, user_service_1.UserService, order_service_1.OrderService,
                            http_1.HTTP_PROVIDERS,
                            router_1.ROUTER_PROVIDERS]
                    }),
                    router_1.RouteConfig([
                        { path: '/welcome', name: 'Welcome', component: welcome_component_1.WelcomeComponent, useAsDefault: true },
                        { path: '/products', name: 'Products', component: product_list_component_1.ProductListComponent },
                        { path: '/products/management', name: 'ProductsManagement', component: product_management_component_1.ProductManagementComponent },
                        { path: '/cart', name: 'Cart', component: cart_component_1.CartComponent },
                        { path: '/order', name: 'Order', component: order_component_1.OrderComponent }
                    ]), 
                    __metadata('design:paramtypes', [])
                ], AppComponent);
                return AppComponent;
            }());
            exports_1("AppComponent", AppComponent);
        }
    }
});
//# sourceMappingURL=app.component.js.map