import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Produto } from '../models/produto.model';
import { Categoria } from '../models/categoria.model';
import { Variacao } from '../models/variacao.model';
import { ProdutoDetalhe } from '../models/produto-detalhe.model';
import { ProdutoCompletoRequest } from '../models/produto-completo.model';

@Injectable({
  providedIn: 'root'
})
export class ProdutoService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080';

  findAllProdutos(): Observable<Produto[]> {
    return this.http.get<Produto[]>(`${this.apiUrl}/produtos`);
  }

  findProdutoById(id: number): Observable<Produto> {
    return this.http.get<Produto>(`${this.apiUrl}/produtos/${id}`);
  }

  findProdutoDetalheById(id: number): Observable<ProdutoDetalhe> {
    return this.http.get<ProdutoDetalhe>(`${this.apiUrl}/produtos/${id}/detalhe`);
  }

  findProdutosByCategoria(categoriaId: number): Observable<Produto[]> {
    return this.http.get<Produto[]>(`${this.apiUrl}/produtos/categorias/${categoriaId}/produtos`);
  }

  findProdutosByUsuario(cpf: number): Observable<Produto[]> {
    return this.http.get<Produto[]>(`${this.apiUrl}/produtos/usuario/${cpf}`);
  }

  saveProduto(produto: Produto): Observable<Produto> {
    return this.http.post<Produto>(`${this.apiUrl}/produtos`, produto);
  }

  saveProdutoCompleto(request: ProdutoCompletoRequest): Observable<ProdutoDetalhe> {
    return this.http.post<ProdutoDetalhe>(`${this.apiUrl}/produtos/completo`, request);
  }

  updateProduto(id: number, produto: Produto): Observable<Produto> {
    return this.http.put<Produto>(`${this.apiUrl}/produtos/${id}`, produto);
  }

  deleteProduto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/produtos/${id}`);
  }

  findAllCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.apiUrl}/produtos/categorias`);
  }

  saveCategoria(categoria: Categoria): Observable<Categoria> {
    return this.http.post<Categoria>(`${this.apiUrl}/produtos/categorias`, categoria);
  }

  findVariacoesByProduto(produtoId: number): Observable<Variacao[]> {
    return this.http.get<Variacao[]>(`${this.apiUrl}/produtos/${produtoId}/variacoes`);
  }

  saveVariacao(variacao: Variacao): Observable<Variacao> {
    return this.http.post<Variacao>(`${this.apiUrl}/produtos/variacoes`, variacao);
  }
}
