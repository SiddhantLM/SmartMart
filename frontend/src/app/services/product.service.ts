import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, of } from 'rxjs';
import { Product } from '../models/product.model';
import { Review } from '../models/review.model';
import { environment } from 'src/environments/environment';
import { API_URL } from './auth.service';


export const APP_URL = environment.apiUrl;

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private readonly CART_KEY = 'cart';

  constructor(private httpClient:HttpClient) {}


  getProductsByCategory(category: string): Observable<Product[]> {
    return this.httpClient.get<Product[]>(`${APP_URL}/products/category/${encodeURIComponent(category)}`);
  }

  getProducts(): Observable<any> {
    return this.httpClient.get<any>(`${APP_URL}/products`);
  }

  getProductByUserId(userId: number): Observable<Product[]> {
    return this.httpClient.get<Product[]>(`${APP_URL}/products/user/${userId}`);
  }

  addProducts(productData:any): Observable<Product> {
    const headers= new HttpHeaders({
      'Content-Type':'application/json'             // ???
    });
    const payload={
      name:productData.name,
      description:productData.description,
      price:productData.price,
      stock:productData.stock,
      category:productData.category,
      base64Image:productData.photoImage
    };
    return this.httpClient.post<Product>(`${APP_URL}/products`, payload, {headers});   // changing this for images
  }

  deleteProduct(id: number): Observable<string> {
    return this.httpClient.delete(`${APP_URL}/product/${id}`, { responseType: 'text' });
  }

  updateProduct(id: number, updatedProduct: Product): Observable<Product> {
    return this.httpClient.put<Product>(`${APP_URL}/products/${id}`, updatedProduct);
  }

  getProductById(productId: number): Observable<Product> {
    return this.httpClient.get<Product>(`${APP_URL}/products/${productId}`);
  }

  addReview(productId:number,userId:number,review:Review):Observable<Product>{
    return this.httpClient.post<Product>(`${APP_URL}/products/${productId}/user/${userId}`,review);
  }

  addToWishlist(productId: number, userId: number): Observable<boolean> {
    return this.httpClient.post<boolean>(`${APP_URL}/products/${productId}/wishlist/user/${userId}`,{});
  }

  getSummary(reviews: Review[]): Observable<string> {
    return this.httpClient.post<{summary: string}>(`${API_URL}/products/summary`, reviews)
      .pipe(
        map(response => response.summary)
      );
}
  
}
