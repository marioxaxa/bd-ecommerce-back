import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PedidoDetalhe } from '../../models/checkout.model';
import { PedidoService } from '../../services/pedido.service';

@Component({
  selector: 'app-checkout-resumo',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './checkout-resumo.component.html'
})
export class CheckoutResumoComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private pedidoService = inject(PedidoService);
  resumo: PedidoDetalhe | null = null;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.pedidoService.findResumoCheckout(id).subscribe(resumo => this.resumo = resumo);
  }
}
