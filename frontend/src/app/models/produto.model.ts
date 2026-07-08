export interface Produto {
  id: number;
  marca: string;
  nome: string;
  fabricante: string;
  categoriaId: number;
  descricao: string;
  usuarioCpf: number | null;
}
