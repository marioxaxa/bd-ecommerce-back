import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { combineLatest } from 'rxjs';
import { Categoria } from '../../models/categoria.model';
import { Produto } from '../../models/produto.model';
import { ProdutoService } from '../../services/produto.service';

@Component({
  selector: 'app-busca',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './busca.component.html'
})
export class BuscaComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private produtoService = inject(ProdutoService);

  termo = '';
  produtos: Produto[] = [];
  categorias: Categoria[] = [];
  fotosPorProduto: Record<number, string> = {};

  ngOnInit(): void {
    combineLatest([
      this.route.queryParamMap,
      this.produtoService.findAllProdutos(),
      this.produtoService.findAllCategorias()
    ]).subscribe(([params, produtos, categorias]) => {
      this.termo = (params.get('q') || '').trim().toLowerCase();
      this.categorias = categorias;
      this.produtos = produtos.filter(produto => {
        const categoria = this.getCategoriaNome(produto.categoriaId).toLowerCase();
        return [produto.nome, produto.marca, produto.fabricante, categoria]
          .some(valor => valor.toLowerCase().includes(this.termo));
      });
      this.carregarFotos(this.produtos);
    });
  }

  getCategoriaNome(categoriaId: number): string {
    return this.categorias.find(c => c.id === categoriaId)?.descricao || 'Oferta';
  }

  private carregarFotos(produtos: Produto[]): void {
    this.fotosPorProduto = {};
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
