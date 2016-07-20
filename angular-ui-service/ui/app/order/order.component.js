/**
 * Created by Flaviu Cicio on 14.07.2016.
 */
System.register(["@angular/router-deprecated", "@angular/core", "./order.service", "../users/user.service"], function(exports_1, context_1) {
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
    var router_deprecated_1, core_1, order_service_1, user_service_1;
    var OrderComponent;
    return {
        setters:[
            function (router_deprecated_1_1) {
                router_deprecated_1 = router_deprecated_1_1;
            },
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (order_service_1_1) {
                order_service_1 = order_service_1_1;
            },
            function (user_service_1_1) {
                user_service_1 = user_service_1_1;
            }],
        execute: function() {
            OrderComponent = (function () {
                function OrderComponent(_orderService, _userService) {
                    this._orderService = _orderService;
                    this._userService = _userService;
                    this.pageTitle = 'Your Orders';
                    this.placeholder = '';
                }
                OrderComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    this._orderService.getOrderByUserId(1)
                        .subscribe(function (orders) { return _this.orders = orders; }, function (error) { return _this.errorMessage = error; });
                };
                OrderComponent = __decorate([
                    core_1.Component({
                        selector: 'pm-orders',
                        templateUrl: 'app/order/order.component.html',
                        directives: [router_deprecated_1.ROUTER_DIRECTIVES]
                    }), 
                    __metadata('design:paramtypes', [order_service_1.OrderService, user_service_1.UserService])
                ], OrderComponent);
                return OrderComponent;
            }());
            exports_1("OrderComponent", OrderComponent);
        }
    }
});
//# sourceMappingURL=order.component.js.map