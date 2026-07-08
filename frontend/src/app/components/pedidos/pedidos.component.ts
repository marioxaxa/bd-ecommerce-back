import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { PedidoDetalhe } from '../../models/checkout.model';
import { PedidoService } from '../../services/pedido.service';

interface PedidoDetalhado extends PedidoDetalhe {
  expandido: boolean;
}

@Component({
  selector: 'app-pedidos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pedidos.component.html'
})
export class PedidosComponent implements OnInit {
  private pedidoService = inject(PedidoService);

  pedidosDetalhados: PedidoDetalhado[] = [];
  carregando = true;

  ngOnInit(): void {
    this.carregarPedidos();
  }

  carregarPedidos(): void {
    this.carregando = true;
    this.pedidoService.findAllPedidos().subscribe({
      next: pedidos => {
        if (pedidos.length === 0) {
          this.pedidosDetalhados = [];
          this.carregando = false;
          return;
        }

        forkJoin(pedidos.map(pedido =>
          this.pedidoService.findResumoCheckout(pedido.codigo).pipe(catchError(() => of(null)))
        )).subscribe(detalhes => {
          this.pedidosDetalhados = detalhes
            .filter((detalhe): detalhe is PedidoDetalhe => detalhe !== null)
            .map(detalhe => ({ ...detalhe, expandido: false }));
          this.carregando = false;
        });
      },
      error: err => {
        console.error('Erro ao carregar pedidos:', err);
        this.carregando = false;
      }
    });
  }

  toggleExpansao(index: number): void {
    this.pedidosDetalhados[index].expandido = !this.pedidosDetalhados[index].expandido;
  }
}
