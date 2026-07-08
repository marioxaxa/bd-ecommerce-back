import { Carrinho } from './carrinho.model';
import { Cupom } from './cupom.model';
import { Endereco } from './endereco.model';
import { Entrega } from './entrega.model';
import { Pagamento } from './pagamento.model';
import { Pedido } from './pedido.model';
import { Produto } from './produto.model';
import { Usuario } from './usuario.model';
import { Variacao } from './variacao.model';

export interface CheckoutItemRequest {
  variacaoId: number;
  quantidade: number;
}

export interface PagamentoCheckoutRequest {
  tipo: 'pix' | 'boleto' | 'cartao';
  titular?: string;
  numero?: number;
  cvv?: number;
  bandeira?: string;
  numeroParcelas?: number;
}

export interface CheckoutRequest {
  usuarioCpf: number;
  enderecoId?: number | null;
  novoEndereco?: Endereco | null;
  cupomCodigo?: string | null;
  pagamento: PagamentoCheckoutRequest;
  itens: CheckoutItemRequest[];
}

export interface CheckoutResponse {
  pedidoCodigo: number;
}

export interface PedidoItemDetalhe {
  produto: Produto;
  variacao: Variacao;
  quantidade: number;
  subtotal: number;
}

export interface PedidoDetalhe {
  pedido: Pedido;
  cliente: Usuario;
  carrinho: Carrinho;
  endereco: Endereco;
  entrega: Entrega;
  pagamento: Pagamento;
  cupom: Cupom;
  itens: PedidoItemDetalhe[];
  subtotal: number;
  desconto: number;
  total: number;
}
