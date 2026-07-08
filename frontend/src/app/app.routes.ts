import { Routes } from '@angular/router';
import { ClientesComponent } from './components/clientes/clientes.component';
import { ProdutosComponent } from './components/produtos/produtos.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { PedidosComponent } from './components/pedidos/pedidos.component';
import { AuthComponent } from './components/auth/auth.component';
import { ProdutoDetalheComponent } from './components/produto-detalhe/produto-detalhe.component';
import { CheckoutResumoComponent } from './components/checkout-resumo/checkout-resumo.component';
import { MinhaContaComponent } from './components/minha-conta/minha-conta.component';
import { BuscaComponent } from './components/busca/busca.component';
import { ProdutoFormComponent } from './components/produto-form/produto-form.component';
import { gerenteGuard } from './guards/gerente.guard';
import { CuponsComponent } from './components/cupons/cupons.component';

export const routes: Routes = [
  { path: '', redirectTo: '/produtos', pathMatch: 'full' },
  { path: 'auth', component: AuthComponent },
  { path: 'clientes', component: ClientesComponent, canActivate: [gerenteGuard] },
  { path: 'cupons', component: CuponsComponent, canActivate: [gerenteGuard] },
  { path: 'minha-conta', component: MinhaContaComponent },
  { path: 'busca', component: BuscaComponent },
  { path: 'produtos', component: ProdutosComponent },
  { path: 'produtos/novo', component: ProdutoFormComponent },
  { path: 'produtos/:id/editar', component: ProdutoFormComponent },
  { path: 'produtos/:id', component: ProdutoDetalheComponent },
  { path: 'checkout', component: CheckoutComponent },
  { path: 'checkout/resumo/:id', component: CheckoutResumoComponent },
  { path: 'pedidos', component: PedidosComponent },
  { path: '**', redirectTo: '/produtos' }
];
