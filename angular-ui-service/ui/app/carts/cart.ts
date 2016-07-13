export interface ICart {
    id: number;
    userid: number;
   	productsList : ICartProduct[];

}

export interface ICartProduct {
	id: number;
    productId: number;
    quantity: number;
    productName: string;
    productPrice : number;
}