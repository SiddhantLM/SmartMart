import { User } from "./user.model";

export interface Review {
    reviewId?:number;
    rating?:number;
    content?:string;
    user?:User
}
