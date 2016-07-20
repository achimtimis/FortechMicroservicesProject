System.register([], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var Product;
    return {
        setters:[],
        execute: function() {
            Product = (function () {
                function Product(id, name, stock, price) {
                    this.id = id;
                    this.name = name;
                    this.stock = stock;
                    this.price = price;
                }
                return Product;
            }());
            exports_1("Product", Product);
        }
    }
});
//# sourceMappingURL=product.js.map