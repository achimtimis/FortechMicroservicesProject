export interface IProduct {
    id: number;
    name: string;
    stock: number;
    price: number;


}

export class Product implements IProduct{
    id:number;
    name:string;
    stock:number;
    price:number;


    constructor(id:number, name:string, stock:number, price:number) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }
}