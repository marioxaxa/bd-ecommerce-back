import { Routes } from '@angular/router';
import { ClientesComponent } from './components/clientes/clientes.component';
import { ProdutosComponent } from './components/produtos/produtos.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { PedidosComponent } from './components/pedidos/pedidos.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'clientes',
    pathMatch: 'full'
  },
  {
    path: 'clientes',
    component: ClientesComponent,
    title: 'Clientes - E-Commerce'
  },
  {
    path: 'produtos',
    component: ProdutosComponent,
    title: 'Produtos - E-Commerce'
  },
  {
    path: 'checkout',
    component: CheckoutComponent,
    title: 'Nova Venda - E-Commerce'
  },
  {
    path: 'pedidos',
    component: PedidosComponent,
    title: 'Histórico de Pedidos - E-Commerce'
  },
  {
    path: '**',
    redirectTo: 'clientes'
  }
];
