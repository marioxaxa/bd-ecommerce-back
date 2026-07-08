import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pedido } from '../models/pedido.model';
import { Cupom } from '../models/cupom.model';
import { Pagamento } from '../models/pagamento.model';
import { Entrega } from '../models/entrega.model';
import { CheckoutRequest, CheckoutResponse, PedidoDetalhe } from '../models/checkout.model';

@Injectable({
  providedIn: 'root'
})
export class PedidoService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080';

  findAllPedidos(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.apiUrl}/pedidos`);
  }

  findPedidoById(codigo: number): Observable<Pedido> {
    return this.http.get<Pedido>(`${this.apiUrl}/pedidos/${codigo}`);
  }

  findPedidosByUsuario(cpf: number): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.apiUrl}/pedidos/usuario/${cpf}`);
  }

  savePedido(pedido: Pedido): Observable<Pedido> {
    return this.http.post<Pedido>(`${this.apiUrl}/pedidos`, pedido);
  }

  findAllCupons(): Observable<Cupom[]> {
    return this.http.get<Cupom[]>(`${this.apiUrl}/pedidos/cupons`);
  }

  findCupomById(codigo: string): Observable<Cupom> {
    return this.http.get<Cupom>(`${this.apiUrl}/pedidos/cupons/${codigo}`);
  }

  saveCupom(cupom: Cupom): Observable<Cupom> {
    return this.http.post<Cupom>(`${this.apiUrl}/pedidos/cupons`, cupom);
  }

  updateCupom(codigo: string, cupom: Cupom): Observable<Cupom> {
    return this.http.put<Cupom>(`${this.apiUrl}/pedidos/cupons/${codigo}`, cupom);
  }

  deleteCupom(codigo: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/pedidos/cupons/${codigo}`);
  }

  findAllPagamentos(): Observable<Pagamento[]> {
    return this.http.get<Pagamento[]>(`${this.apiUrl}/pedidos/pagamentos`);
  }

  savePagamento(pagamento: Pagamento): Observable<Pagamento> {
    return this.http.post<Pagamento>(`${this.apiUrl}/pedidos/pagamentos`, pagamento);
  }

  findAllEntregas(): Observable<Entrega[]> {
    return this.http.get<Entrega[]>(`${this.apiUrl}/pedidos/entregas`);
  }

  saveEntrega(entrega: Entrega): Observable<Entrega> {
    return this.http.post<Entrega>(`${this.apiUrl}/pedidos/entregas`, entrega);
  }

  finalizarCheckout(request: CheckoutRequest): Observable<CheckoutResponse> {
    return this.http.post<CheckoutResponse>(`${this.apiUrl}/checkout/finalizar`, request);
  }

  findResumoCheckout(pedidoCodigo: number): Observable<PedidoDetalhe> {
    return this.http.get<PedidoDetalhe>(`${this.apiUrl}/checkout/resumo/${pedidoCodigo}`);
  }
}
