import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrderService, OrderResponse } from '../../core/services/order.service';
import { ToastService } from '../../shared/services/toast.service';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit {
  orders: OrderResponse[] = [];
  loading = false;

  constructor(private orderService: OrderService, private toast: ToastService) {}

  ngOnInit(): void {
    this.loading = true;
    this.orderService.getMyOrders().subscribe({
      next: (orders) => { this.orders = orders.reverse(); this.loading = false; },
      error: (err) => { this.toast.error(err.error?.message || 'Failed to load orders'); this.loading = false; }
    });
  }

  statusClass(status: string): string {
    switch (status) {
      case 'DELIVERED': return 'status-delivered';
      case 'SHIPPED': return 'status-shipped';
      case 'PAID': return 'status-paid';
      case 'CANCELLED': return 'status-cancelled';
      default: return 'status-pending';
    }
  }
}
