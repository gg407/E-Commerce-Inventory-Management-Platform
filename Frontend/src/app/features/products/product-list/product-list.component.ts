import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService, Product, PagedResponse } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../shared/services/toast.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  totalPages = 0;
  currentPage = 0;
  pageSize = 9;
  loading = false;
  error: string | null = null;
  skeletons = Array.from({ length: 6 });
  addingId: number | null = null;

  constructor(
    private productService: ProductService,
    private cartService: CartService,
    public authService: AuthService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.error = null;
    this.productService.getProducts(this.currentPage, this.pageSize).subscribe({
      next: (res: PagedResponse<Product>) => {
        this.products = res.content;
        this.totalPages = res.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to load products';
        this.loading = false;
        this.toast.error(this.error!);
      }
    });
  }

  addToCart(product: Product): void {
    if (!this.authService.isLoggedIn) {
      this.toast.info('Please log in to add items to your cart');
      return;
    }
    this.addingId = product.id;
    this.cartService.addItem(product.id, 1).subscribe({
      next: () => {
        this.addingId = null;
        this.toast.success(`${product.name} added to cart`);
      },
      error: (err) => {
        this.addingId = null;
        this.toast.error(err.error?.message || 'Failed to add item');
      }
    });
  }

  onPageChange(page: number): void {
    if (page < 0 || page >= this.totalPages) return;
    this.currentPage = page;
    this.loadProducts();
  }

  trackById(index: number, product: Product): number {
    return product.id;
  }
}
