import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Endereco } from '../../models/endereco.model';
import { Pedido } from '../../models/pedido.model';
import { Produto } from '../../models/produto.model';
import { AuthService } from '../../services/auth.service';
import { EnderecoService } from '../../services/endereco.service';
import { PedidoService } from '../../services/pedido.service';
import { ProdutoService } from '../../services/produto.service';

@Component({
  selector: 'app-minha-conta',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './minha-conta.component.html'
})
export class MinhaContaComponent implements OnInit {
  private authService = inject(AuthService);
  private enderecoService = inject(EnderecoService);
  private pedidoService = inject(PedidoService);
  private produtoService = inject(ProdutoService);
  private router = inject(Router);

  pedidos: Pedido[] = [];
  enderecos: Endereco[] = [];
  produtos: Produto[] = [];
  mensagemConta = '';
  mensagemEndereco = '';

  conta = { username: '', senha: '' };
  novoEndereco: Endereco = { id: 0, nome: 'Casa', logradouro: '', numero: 0, bairro: '', cep: 0, usuarioCpf: 0 };

  get usuario() {
    return this.authService.usuarioAtual;
  }

  ngOnInit(): void {
    if (!this.usuario) {
      this.router.navigateByUrl('/auth');
      return;
    }
    this.conta = { username: this.usuario.username, senha: '' };
    this.novoEndereco.usuarioCpf = this.usuario.cpf;
    this.pedidoService.findPedidosByUsuario(this.usuario.cpf).subscribe(pedidos => this.pedidos = pedidos);
    this.enderecoService.findByUsuario(this.usuario.cpf).subscribe(enderecos => this.enderecos = enderecos);
    this.produtoService.findProdutosByUsuario(this.usuario.cpf).subscribe(produtos => this.produtos = produtos);
  }

  salvarConta(): void {
    if (!this.usuario) return;
    this.authService.atualizar({ ...this.usuario, username: this.conta.username, senha: this.conta.senha }).subscribe({
      next: () => this.mensagemConta = 'Conta atualizada.',
      error: () => this.mensagemConta = 'Não foi possível atualizar a conta.'
    });
  }

  cadastrarEndereco(): void {
    if (!this.usuario) return;
    this.enderecoService.save({ ...this.novoEndereco, usuarioCpf: this.usuario.cpf }).subscribe({
      next: endereco => {
        this.enderecos = [...this.enderecos, endereco];
        this.mensagemEndereco = 'Endereço cadastrado.';
        this.novoEndereco = { id: 0, nome: 'Casa', logradouro: '', numero: 0, bairro: '', cep: 0, usuarioCpf: this.usuario!.cpf };
      },
      error: () => this.mensagemEndereco = 'Não foi possível cadastrar o endereço.'
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigateByUrl('/produtos');
  }
}
