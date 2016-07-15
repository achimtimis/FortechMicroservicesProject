import {IProduct} from "../products/product";
/**
 * 
 * Created by Flaviu Cicio on 14.07.2016.
 */

export interface IOrder{
    id: number;
    userId: number;
    date: Date;
    products: IOrderProduct[];
}

export interface IOrderProduct{
    id: number;
    productId: number;
    productName: string;
    productPrice: number;
    quantity: number
}