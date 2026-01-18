import { Address } from "./address.model";
import { Cart } from "./cart.model";
import { Feedback } from "./feedback.model";
import { Order } from "./order.model";
import { Product } from "./product.model";

export interface User {
    userId?:number;
    email?:string;
    password?:string
    username?:string;
    mobileNumber?:string;
    userRole?:string
    addresses?:Address[];
    orders?:Order[];
    cart?:Cart;
    wishlist?:Product[];
}
