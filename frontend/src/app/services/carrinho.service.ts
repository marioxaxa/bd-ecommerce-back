import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Carrinho } from '../models/carrinho.model';
import { Contem } from '../models/contem.model';

@Injectable({
  providedIn: 'root'
})
export class CarrinhoService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/carrinhos';

  findAll(): Observable<Carrinho[]> {
    return this.http.get<Carrinho[]>(this.apiUrl);
  }

  findById(id: number): Observable<Carrinho> {
    return this.http.get<Carrinho>(`${this.apiUrl}/${id}`);
  }

  findByUsuario(usuarioCpf: number): Observable<Carrinho[]> {
    return this.http.get<Carrinho[]>(`${this.apiUrl}/usuario/${usuarioCpf}`);
  }

  save(carrinho: Carrinho): Observable<Carrinho> {
    return this.http.post<Carrinho>(this.apiUrl, carrinho);
  }

  findItensByCarrinho(carrinhoId: number): Observable<Contem[]> {
    return this.http.get<Contem[]>(`${this.apiUrl}/${carrinhoId}/itens`);
  }

  addItem(carrinhoId: number, item: Omit<Contem, 'carrinhoId'>): Observable<Contem> {
    return this.http.post<Contem>(`${this.apiUrl}/${carrinhoId}/itens`, item);
  }

  removeItem(carrinhoId: number, variacaoId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${carrinhoId}/itens/${variacaoId}`);
  }
}
