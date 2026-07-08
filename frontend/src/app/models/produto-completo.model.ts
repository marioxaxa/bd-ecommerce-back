import { Atributo } from './atributo.model';
import { Categoria } from './categoria.model';
import { Produto } from './produto.model';
import { Variacao } from './variacao.model';

export interface ProdutoCompletoRequest {
  produto: Produto;
  categoria: Categoria | null;
  atributos: Atributo[];
  variacoes: Variacao[];
}
