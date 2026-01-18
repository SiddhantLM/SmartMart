import { User } from "./user.model";

export interface Feedback {
    feedbackId?:number;
    message?:string;
    rating?:number;
    user?:User;
}
