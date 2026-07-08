import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Endereco } from '../../models/endereco.model';
import { CheckoutRequest, PagamentoCheckoutRequest } from '../../models/checkout.model';
import { AuthService } from '../../services/auth.service';
import { CartItem, CartService } from '../../services/cart.service';
import { EnderecoService } from '../../services/endereco.service';
import { PedidoService } from '../../services/pedido.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './checkout.component.html'
})
export class CheckoutComponent implements OnInit {
  private authService = inject(AuthService);
  private cartService = inject(CartService);
  private enderecoService = inject(EnderecoService);
  private pedidoService = inject(PedidoService);
  private router = inject(Router);

  itens: CartItem[] = [];
  enderecos: Endereco[] = [];
  enderecoId: number | null = null;
  usarNovoEndereco = false;
  cupomTexto = '';
  cupomCodigo: string | null = null;
  desconto = 0;
  mensagemCupom = '';
  processando = false;

  novoEndereco: Endereco = { id: 0, nome: 'Casa', logradouro: '', numero: 0, bairro: '', cep: 0, usuarioCpf: 0 };
  pagamento: PagamentoCheckoutRequest = { tipo: 'pix', numeroParcelas: 1 };

  get usuario() {
    return this.authService.usuarioAtual;
  }

  ngOnInit(): void {
    this.itens = this.cartService.items;
    if (this.usuario) {
      this.novoEndereco.usuarioCpf = this.usuario.cpf;
      this.enderecoService.findByUsuario(this.usuario.cpf).subscribe(enderecos => {
        this.enderecos = enderecos;
        this.enderecoId = enderecos[0]?.id || null;
      });
    }
  }

  subtotal(): number {
    return this.itens.reduce((total, item) => total + item.variacao.preco * item.quantidade, 0);
  }

  total(): number {
    return Math.max(0, this.subtotal() - this.desconto);
  }

  remover(variacaoId: number): void {
    this.cartService.removeItem(variacaoId);
    this.itens = this.cartService.items;
  }

  validarCupom(): void {
    const codigo = this.cupomTexto.trim();
    this.mensagemCupom = '';
    this.cupomCodigo = null;
    this.desconto = 0;
    if (!codigo) {
      this.mensagemCupom = 'Digite um código de cupom.';
      return;
    }
    this.pedidoService.findCupomById(codigo).subscribe({
      next: cupom => {
        const ativo = cupom.quantidade > 0 && new Date(cupom.validade).getTime() > Date.now();
        if (!ativo) {
          this.mensagemCupom = 'Cupom inativo ou expirado.';
          return;
        }
        this.cupomCodigo = cupom.codigo;
        this.desconto = cupom.valor;
        this.mensagemCupom = `Cupom aplicado: R$ ${cupom.valor.toFixed(2)} de desconto.`;
      },
      error: () => this.mensagemCupom = 'Cupom não encontrado.'
    });
  }

  finalizarCompra(): void {
    if (!this.usuario) {
      this.router.navigateByUrl('/auth');
      return;
    }
    if (this.itens.length === 0) return;
    this.processando = true;
    const request: CheckoutRequest = {
      usuarioCpf: this.usuario.cpf,
      enderecoId: this.usarNovoEndereco ? null : this.enderecoId,
      novoEndereco: this.usarNovoEndereco ? { ...this.novoEndereco, usuarioCpf: this.usuario.cpf } : null,
      cupomCodigo: this.cupomCodigo,
      pagamento: this.pagamento,
      itens: this.itens.map(item => ({ variacaoId: item.variacao.id, quantidade: item.quantidade }))
    };
    this.pedidoService.finalizarCheckout(request).subscribe({
      next: response => {
        this.cartService.clear();
        this.router.navigate(['/checkout/resumo', response.pedidoCodigo]);
      },
      error: () => {
        this.processando = false;
        alert('Não foi possível finalizar a compra. Confira endereço, pagamento e estoque.');
      }
    });
  }
}
