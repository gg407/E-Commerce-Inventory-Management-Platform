import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { CartService } from '../../../core/services/cart.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  bump = false;
  private lastCount = 0;

  constructor(public authService: AuthService, public cartService: CartService) {}

  ngOnInit(): void {
    this.cartService.cart$.subscribe(() => {
      const count = this.cartService.itemCount;
      if (count !== this.lastCount) {
        this.bump = true;
        setTimeout(() => this.bump = false, 400);
        this.lastCount = count;
      }
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
