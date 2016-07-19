System.register(["./product", "./product-crud.service", "@angular/core"], function(exports_1, context_1) {
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
    var product_1, product_crud_service_1, core_1;
    var ProductFormComponent;
    return {
        setters:[
            function (product_1_1) {
                product_1 = product_1_1;
            },
            function (product_crud_service_1_1) {
                product_crud_service_1 = product_crud_service_1_1;
            },
            function (core_1_1) {
                core_1 = core_1_1;
            }],
        execute: function() {
            ProductFormComponent = (function () {
                function ProductFormComponent(_productCrudService) {
                    this._productCrudService = _productCrudService;
                    this.model = new product_1.Product(null, '', 0, 0);
                    this.submitted = false;
                }
                ProductFormComponent.prototype.ngOnInit = function () {
                    return undefined;
                };
                ProductFormComponent.prototype.onSubmit = function () {
                    console.log(this.model);
                    this._productCrudService.addProduct(this.model);
                    this.submitted = true;
                    location.reload();
                };
                Object.defineProperty(ProductFormComponent.prototype, "diagnostic", {
                    // TODO: Remove this when we're done
                    get: function () { return JSON.stringify(this.model); },
                    enumerable: true,
                    configurable: true
                });
                ProductFormComponent.prototype.editMode = function () {
                    if (this.model.id != null) {
                        return true;
                    }
                    return false;
                };
                ProductFormComponent.prototype.setModelId = function (id) {
                    this.model = new product_1.Product(2, '', 0, 0);
                    console.log("ProductFormComponent -> setModelId: " + id);
                    console.log("ProductFormComponent -> setModelId: " + this.model.id);
                };
                ProductFormComponent = __decorate([
                    core_1.Component({
                        selector: 'product-form',
                        templateUrl: 'app/products/product-form.component.html'
                    }), 
                    __metadata('design:paramtypes', [product_crud_service_1.ProductCrudService])
                ], ProductFormComponent);
                return ProductFormComponent;
            }());
            exports_1("ProductFormComponent", ProductFormComponent);
        }
    }
});
//# sourceMappingURL=product-form.component.js.map