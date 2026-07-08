import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './auth.component.html'
})
export class AuthComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  loginEmail = '';
  loginSenha = '';
  erro = '';

  novoUsuario: Usuario = {
    cpf: 0,
    email: '',
    senha: '',
    username: '',
    gerente: false
  };

  entrar(): void {
    this.erro = '';
    this.authService.login(this.loginEmail, this.loginSenha).subscribe({
      next: () => this.router.navigateByUrl('/produtos'),
      error: (err) => this.erro = err.message || 'Não foi possível entrar.'
    });
  }

  cadastrar(): void {
    this.erro = '';
    this.authService.cadastrar(this.novoUsuario).subscribe({
      next: () => this.router.navigateByUrl('/produtos'),
      error: () => this.erro = 'Não foi possível criar o usuário.'
    });
  }
}
