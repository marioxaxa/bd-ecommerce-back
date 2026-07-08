import { Atributo } from './atributo.model';
import { Categoria } from './categoria.model';
import { Produto } from './produto.model';
import { Variacao } from './variacao.model';

export interface ProdutoDetalhe {
  produto: Produto;
  categoria: Categoria;
  atributos: Atributo[];
  variacoes: Variacao[];
}
