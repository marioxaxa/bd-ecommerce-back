import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Produto } from '../models/produto.model';
import { Variacao } from '../models/variacao.model';

export interface CartItem {
  produto: Produto;
  variacao: Variacao;
  quantidade: number;
}

const STORAGE_KEY = 'marcel_carrinho_atual';

@Injectable({ providedIn: 'root' })
export class CartService {
  private itemsSubject = new BehaviorSubject<CartItem[]>(this.getStoredItems());
  items$ = this.itemsSubject.asObservable();

  get items(): CartItem[] {
    return this.itemsSubject.value;
  }

  addItem(item: CartItem): boolean {
    const items = [...this.items];
    const existing = items.find(i => i.variacao.id === item.variacao.id);
    if (existing) {
      if (existing.quantidade + item.quantidade > item.variacao.estoque) {
        existing.quantidade = item.variacao.estoque;
        this.setItems(items);
        return false;
      }
      existing.quantidade += item.quantidade;
    } else {
      if (item.quantidade > item.variacao.estoque) {
        item = { ...item, quantidade: item.variacao.estoque };
        items.push(item);
        this.setItems(items);
        return false;
      }
      items.push(item);
    }
    this.setItems(items);
    return true;
  }

  removeItem(variacaoId: number): void {
    this.setItems(this.items.filter(item => item.variacao.id !== variacaoId));
  }

  clear(): void {
    this.setItems([]);
  }

  total(): number {
    return this.items.reduce((total, item) => total + item.variacao.preco * item.quantidade, 0);
  }

  private setItems(items: CartItem[]): void {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(items));
    this.itemsSubject.next(items);
  }

  private getStoredItems(): CartItem[] {
    const value = localStorage.getItem(STORAGE_KEY);
    return value ? JSON.parse(value) as CartItem[] : [];
  }
}
