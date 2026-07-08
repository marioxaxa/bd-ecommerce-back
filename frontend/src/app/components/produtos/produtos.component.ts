import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Produto } from '../../models/produto.model';
import { Categoria } from '../../models/categoria.model';
import { ProdutoService } from '../../services/produto.service';

@Component({
  selector: 'app-produtos',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './produtos.component.html'
})
export class ProdutosComponent implements OnInit {
  private produtoService = inject(ProdutoService);

  produtos: Produto[] = [];
  categorias: Categoria[] = [];
  fotosPorProduto: Record<number, string> = {};

  ngOnInit(): void {
    this.produtoService.findAllProdutos().subscribe(data => {
      this.produtos = data;
      this.carregarFotos(data);
    });
    this.produtoService.findAllCategorias().subscribe(data => this.categorias = data);
  }

  getCategoriaNome(categoriaId: number): string {
    return this.categorias.find(c => c.id === categoriaId)?.descricao || 'Oferta';
  }

  private carregarFotos(produtos: Produto[]): void {
    produtos.forEach(produto => {
      this.produtoService.findVariacoesByProduto(produto.id).subscribe(variacoes => {
        const foto = variacoes[0]?.fotoUrl;
        if (foto) {
          this.fotosPorProduto = { ...this.fotosPorProduto, [produto.id]: foto };
        }
      });
    });
  }
}
