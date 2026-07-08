import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/usuarios';

  findAll(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiUrl);
  }

  findById(cpf: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${cpf}`);
  }

  save(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(this.apiUrl, usuario);
  }

  login(email: string, senha: string): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/login`, { email, senha });
  }

  update(cpf: number, usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${cpf}`, usuario);
  }

  delete(cpf: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${cpf}`);
  }
}
