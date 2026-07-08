import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Atributo } from '../../models/atributo.model';
import { Variacao } from '../../models/variacao.model';
import { AuthService } from '../../services/auth.service';
import { ProdutoService } from '../../services/produto.service';

@Component({
  selector: 'app-produto-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './produto-form.component.html'
})
export class ProdutoFormComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);
  private produtoService = inject(ProdutoService);

  editando = false;
  mensagem = '';
  produto = { id: 0, nome: '', marca: '', fabricante: '', categoriaId: 0, descricao: '', usuarioCpf: null as number | null };
  categoria = { id: 0, descricao: '' };
  atributos: Atributo[] = [{ id: 0, key: '', valor: '' }];
  variacoes: Variacao[] = [{ id: 0, nome: '', preco: 0, estoque: 0, fotoUrl: '', produtoId: 0 }];

  ngOnInit(): void {
    const usuario = this.authService.usuarioAtual;
    if (!usuario) {
      this.router.navigateByUrl('/auth');
      return;
    }
    this.produto.usuarioCpf = usuario.cpf;
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.editando = true;
      this.produtoService.findProdutoDetalheById(id).subscribe(detalhe => {
        this.produto = detalhe.produto;
        this.categoria = detalhe.categoria;
        this.atributos = detalhe.atributos.length ? detalhe.atributos : this.atributos;
        this.variacoes = detalhe.variacoes.length ? detalhe.variacoes : this.variacoes;
      });
    }
  }

  adicionarAtributo(): void {
    this.atributos.push({ id: 0, key: '', valor: '' });
  }

  removerAtributo(index: number): void {
    this.atributos.splice(index, 1);
  }

  adicionarVariacao(): void {
    this.variacoes.push({ id: 0, nome: '', preco: 0, estoque: 0, fotoUrl: '', produtoId: this.produto.id });
  }

  removerVariacao(index: number): void {
    this.variacoes.splice(index, 1);
  }

  salvar(): void {
    if (this.editando) {
      this.produtoService.updateProduto(this.produto.id, this.produto).subscribe({
        next: produto => this.router.navigate(['/produtos', produto.id]),
        error: () => this.mensagem = 'Não foi possível atualizar o produto.'
      });
      return;
    }

    this.produtoService.saveProdutoCompleto({
      produto: { ...this.produto, categoriaId: this.categoria.id || this.produto.categoriaId },
      categoria: this.categoria.descricao ? this.categoria : null,
      atributos: this.atributos.filter(atributo => atributo.key && atributo.valor),
      variacoes: this.variacoes.filter(variacao => variacao.nome)
    }).subscribe({
      next: detalhe => this.router.navigate(['/produtos', detalhe.produto.id]),
      error: () => this.mensagem = 'Não foi possível salvar o produto.'
    });
  }
}
