import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Usuario } from '../../models/usuario.model';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clientes.component.html'
})
export class ClientesComponent implements OnInit {
  private usuarioService = inject(UsuarioService);

  usuarios: Usuario[] = [];
  novoUsuario: Usuario = {
    cpf: 0,
    email: '',
    senha: '',
    username: '',
    gerente: false
  };

  ngOnInit(): void {
    this.carregarUsuarios();
  }

  carregarUsuarios(): void {
    this.usuarioService.findAll().subscribe({
      next: (data) => this.usuarios = data,
      error: (err) => console.error('Erro ao carregar usuários:', err)
    });
  }

  salvarUsuario(): void {
    if (!this.novoUsuario.cpf || !this.novoUsuario.email || !this.novoUsuario.username) {
      alert('Preencha todos os campos obrigatórios!');
      return;
    }

    this.usuarioService.save(this.novoUsuario).subscribe({
      next: () => {
        this.carregarUsuarios();
        this.limparFormulario();
      },
      error: (err) => {
        console.error('Erro ao salvar usuário:', err);
        alert('Erro ao salvar usuário!');
      }
    });
  }

  excluirUsuario(cpf: number): void {
    if (confirm('Deseja realmente excluir este usuário?')) {
      this.usuarioService.delete(cpf).subscribe({
        next: () => this.carregarUsuarios(),
        error: (err) => {
          console.error('Erro ao excluir usuário:', err);
          alert('Erro ao excluir usuário!');
        }
      });
    }
  }

  limparFormulario(): void {
    this.novoUsuario = {
      cpf: 0,
      email: '',
      senha: '',
      username: '',
      gerente: false
    };
  }
}
