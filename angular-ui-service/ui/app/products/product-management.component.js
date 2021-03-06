System.register(["@angular/core", "./product", "@angular/router-deprecated", "./product-crud.service", "./product-form.component"], function(exports_1, context_1) {
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
    var core_1, product_1, router_deprecated_1, product_crud_service_1, product_form_component_1;
    var ProductManagementComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (product_1_1) {
                product_1 = product_1_1;
            },
            function (router_deprecated_1_1) {
                router_deprecated_1 = router_deprecated_1_1;
            },
            function (product_crud_service_1_1) {
                product_crud_service_1 = product_crud_service_1_1;
            },
            function (product_form_component_1_1) {
                product_form_component_1 = product_form_component_1_1;
            }],
        execute: function() {
            /**
             * Created by Flaviu Cicio on 15.07.2016.
             */
            ProductManagementComponent = (function () {
                function ProductManagementComponent(_productCrudService) {
                    this._productCrudService = _productCrudService;
                    this.errorMessage = '';
                    this.pageTitle = 'Product Management';
                    this.model = new product_1.Product(null, "", -1, -1);
                }
                ProductManagementComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    this._productCrudService.getAllProductsInfo()
                        .subscribe(function (products) { return _this.products = products; }, function (error) { return _this.errorMessage = error; });
                };
                ProductManagementComponent.prototype.deleteProduct = function (id) {
                    console.log("ProductManagement -> DeleteProduct with id: " + id);
                    this._productCrudService.removeProduct(id);
                    location.reload();
                    location.reload();
                };
                ProductManagementComponent.prototype.setEditProduct = function (id, name, stock, price) {
                    this.model = new product_1.Product(id, name, stock, price);
                };
                ProductManagementComponent.prototype.unsetEditProduct = function () {
                    this.model = new product_1.Product(null, '', -1, -1);
                };
                ProductManagementComponent.prototype.editMode = function () {
                    if (this.model.id == null) {
                        return false;
                    }
                    return true;
                };
                ProductManagementComponent.prototype.getModelId = function () {
                    return this.model.id;
                };
                ProductManagementComponent = __decorate([
                    core_1.Component({
                        selector: 'products-management',
                        templateUrl: 'app/products/product-management.component.html',
                        directives: [router_deprecated_1.ROUTER_DIRECTIVES, product_form_component_1.ProductFormComponent],
                        providers: [product_crud_service_1.ProductCrudService, product_form_component_1.ProductFormComponent]
                    }), 
                    __metadata('design:paramtypes', [product_crud_service_1.ProductCrudService])
                ], ProductManagementComponent);
                return ProductManagementComponent;
            }());
            exports_1("ProductManagementComponent", ProductManagementComponent);
        }
    }
});
//# sourceMappingURL=product-management.component.js.map