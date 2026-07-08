import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProdutoDetalhe } from '../../models/produto-detalhe.model';
import { Variacao } from '../../models/variacao.model';
import { ProdutoService } from '../../services/produto.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-produto-detalhe',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './produto-detalhe.component.html'
})
export class ProdutoDetalheComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private produtoService = inject(ProdutoService);
  private cartService = inject(CartService);

  detalhe: ProdutoDetalhe | null = null;
  variacaoSelecionada: Variacao | null = null;
  quantidade = 1;
  mensagem = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.produtoService.findProdutoDetalheById(id).subscribe(detalhe => {
      this.detalhe = detalhe;
      this.variacaoSelecionada = detalhe.variacoes[0] || null;
    });
  }

  selecionar(variacao: Variacao): void {
    this.variacaoSelecionada = variacao;
  }

  adicionarAoCarrinho(): void {
    if (!this.detalhe || !this.variacaoSelecionada) return;
    const quantidade = Math.max(1, Math.min(this.quantidade, this.variacaoSelecionada.estoque));
    const adicionado = this.cartService.addItem({ produto: this.detalhe.produto, variacao: this.variacaoSelecionada, quantidade });
    this.quantidade = quantidade;
    this.mensagem = adicionado ? 'Produto adicionado ao carrinho.' : 'Quantidade ajustada ao estoque disponível.';
  }
}
