import { Product } from "./product.model";

export interface CartItem {
    id?:Number;
    product?:Product;
    quantity?:number;
}
