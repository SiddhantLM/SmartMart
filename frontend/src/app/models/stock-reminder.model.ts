import { Product } from "./product.model";

export interface StockReminder {
    id?:Number;
    email?:string;
    product?:Product;
}
