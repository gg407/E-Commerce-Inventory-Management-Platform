import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  stockQuantity: number;
  categoryId: number;
  categoryName: string;
  createdAt: string;
}

export interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly API = `${environment.apiUrl}/api/v1/products`;

  constructor(private http: HttpClient) {}

  getProducts(page = 0, size = 10, sortBy = 'id'): Observable<PagedResponse<Product>> {
    const params = new HttpParams().set('page', page).set('size', size).set('sortBy', sortBy);
    return this.http.get<PagedResponse<Product>>(this.API, { params });
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.API}/${id}`);
  }

  createProduct(product: Partial<Product>): Observable<Product> {
    return this.http.post<Product>(this.API, product);
  }

  updateProduct(id: number, product: Partial<Product>): Observable<Product> {
    return this.http.put<Product>(`${this.API}/${id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}
