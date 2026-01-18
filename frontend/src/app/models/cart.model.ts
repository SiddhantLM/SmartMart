import { CartItem } from "./cart-item.model";

export interface Cart {
    cartId?:Number;
    totalAmount?:number;
    quantity?:number;
    items?:CartItem[];
    createdAt?:string;
    updatedAt?:string;
}
