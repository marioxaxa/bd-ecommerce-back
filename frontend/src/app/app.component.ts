import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from './services/auth.service';
import { CartService } from './services/cart.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html'
})
export class AppComponent {
  title = 'Marcel Pechinchas';
  termoBusca = '';

  constructor(public authService: AuthService, public cartService: CartService, private router: Router) {}

  buscar(): void {
    const q = this.termoBusca.trim();
    if (q) {
      this.router.navigate(['/busca'], { queryParams: { q } });
    }
  }

  sair(): void {
    this.authService.logout();
  }
}
