import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Cupom } from '../../models/cupom.model';
import { PedidoService } from '../../services/pedido.service';

@Component({
  selector: 'app-cupons',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cupons.component.html'
})
export class CuponsComponent implements OnInit {
  private pedidoService = inject(PedidoService);

  cupons: Cupom[] = [];
  editando = false;
  cupom: Cupom = this.novoCupom();

  ngOnInit(): void {
    this.carregarCupons();
  }

  carregarCupons(): void {
    this.pedidoService.findAllCupons().subscribe(cupons => this.cupons = cupons);
  }

  salvarCupom(): void {
    const request = { ...this.cupom, validade: this.normalizarValidade(this.cupom.validade) };
    const operacao = this.editando
      ? this.pedidoService.updateCupom(request.codigo, request)
      : this.pedidoService.saveCupom(request);

    operacao.subscribe({
      next: () => {
        this.carregarCupons();
        this.limparFormulario();
      },
      error: () => alert('Erro ao salvar cupom.')
    });
  }

  editarCupom(cupom: Cupom): void {
    this.editando = true;
    this.cupom = { ...cupom, validade: cupom.validade.slice(0, 16) };
  }

  excluirCupom(codigo: string): void {
    if (!confirm('Deseja realmente excluir este cupom?')) return;
    this.pedidoService.deleteCupom(codigo).subscribe({
      next: () => this.carregarCupons(),
      error: () => alert('Erro ao excluir cupom.')
    });
  }

  limparFormulario(): void {
    this.editando = false;
    this.cupom = this.novoCupom();
  }

  private novoCupom(): Cupom {
    return { codigo: '', valor: 0, quantidade: 1, validade: '' };
  }

  private normalizarValidade(validade: string): string {
    return validade.length === 16 ? `${validade}:00` : validade;
  }
}
