import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { CartService, CartResponse } from '../../core/services/cart.service';
import { OrderService } from '../../core/services/order.service';
import { ToastService } from '../../shared/services/toast.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  cart: CartResponse | null = null;
  loading = false;
  placingOrder = false;

  constructor(
    private cartService: CartService,
    private orderService: OrderService,
    private router: Router,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.loading = true;
    this.cartService.loadCart().subscribe({
      next: (cart) => { this.cart = cart; this.loading = false; },
      error: (err) => { this.toast.error(err.error?.message || 'Failed to load cart'); this.loading = false; }
    });
  }

  updateQuantity(itemId: number, quantity: number): void {
    if (quantity < 1) return;
    this.cartService.updateItem(itemId, quantity).subscribe({
      next: (cart) => this.cart = cart,
      error: (err) => this.toast.error(err.error?.message || 'Failed to update item')
    });
  }

  removeItem(itemId: number): void {
    this.cartService.removeItem(itemId).subscribe({
      next: (cart) => { this.cart = cart; this.toast.info('Item removed'); },
      error: (err) => this.toast.error(err.error?.message || 'Failed to remove item')
    });
  }

  checkout(): void {
    this.placingOrder = true;
    this.orderService.placeOrder().subscribe({
      next: () => {
        this.placingOrder = false;
        this.toast.success('Order placed successfully! 🎉');
        this.router.navigate(['/orders']);
      },
      error: (err) => {
        this.placingOrder = false;
        this.toast.error(err.error?.message || 'Failed to place order');
      }
    });
  }
}
