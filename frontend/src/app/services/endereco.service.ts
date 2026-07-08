import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Endereco } from '../models/endereco.model';

@Injectable({ providedIn: 'root' })
export class EnderecoService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/enderecos';

  findByUsuario(usuarioCpf: number): Observable<Endereco[]> {
    return this.http.get<Endereco[]>(`${this.apiUrl}/usuario/${usuarioCpf}`);
  }

  save(endereco: Endereco): Observable<Endereco> {
    return this.http.post<Endereco>(this.apiUrl, endereco);
  }
}
