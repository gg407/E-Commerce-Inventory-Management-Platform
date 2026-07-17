import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface CartItem {
  id: number;
  productId: number;
  productName: string;
  unitPrice: number;
  quantity: number;
}

export interface CartResponse {
  id: number;
  items: CartItem[];
  totalAmount: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly API = `${environment.apiUrl}/api/v1/cart`;
  private cartSubject = new BehaviorSubject<CartResponse | null>(null);
  cart$ = this.cartSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadCart(): Observable<CartResponse> {
    return this.http.get<CartResponse>(this.API).pipe(tap(cart => this.cartSubject.next(cart)));
  }

  addItem(productId: number, quantity: number): Observable<CartResponse> {
    return this.http.post<CartResponse>(`${this.API}/items`, { productId, quantity })
      .pipe(tap(cart => this.cartSubject.next(cart)));
  }

  updateItem(itemId: number, quantity: number): Observable<CartResponse> {
    return this.http.put<CartResponse>(`${this.API}/items/${itemId}?quantity=${quantity}`, {})
      .pipe(tap(cart => this.cartSubject.next(cart)));
  }

  removeItem(itemId: number): Observable<CartResponse> {
    return this.http.delete<CartResponse>(`${this.API}/items/${itemId}`)
      .pipe(tap(cart => this.cartSubject.next(cart)));
  }

  get itemCount(): number {
    return this.cartSubject.value?.items?.reduce((sum, i) => sum + i.quantity, 0) ?? 0;
  }
}
