import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { Usuario } from '../models/usuario.model';
import { UsuarioService } from './usuario.service';

const STORAGE_KEY = 'marcel_usuario_atual';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private usuarioService = inject(UsuarioService);
  private usuarioSubject = new BehaviorSubject<Usuario | null>(this.getStoredUser());
  usuarioAtual$ = this.usuarioSubject.asObservable();

  get usuarioAtual(): Usuario | null {
    return this.usuarioSubject.value;
  }

  login(email: string, senha: string): Observable<Usuario> {
    return this.usuarioService.login(email, senha).pipe(map(usuario => {
      this.setUsuario(usuario);
      return usuario;
    }));
  }

  cadastrar(usuario: Usuario): Observable<Usuario> {
    return this.usuarioService.save(usuario).pipe(map(novoUsuario => {
      this.setUsuario(novoUsuario);
      return novoUsuario;
    }));
  }

  atualizar(usuario: Usuario): Observable<Usuario> {
    return this.usuarioService.update(usuario.cpf, usuario).pipe(map(usuarioAtualizado => {
      this.setUsuario(usuarioAtualizado);
      return usuarioAtualizado;
    }));
  }

  logout(): void {
    localStorage.removeItem(STORAGE_KEY);
    this.usuarioSubject.next(null);
  }

  private setUsuario(usuario: Usuario): void {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(usuario));
    this.usuarioSubject.next(usuario);
  }

  private getStoredUser(): Usuario | null {
    const value = localStorage.getItem(STORAGE_KEY);
    return value ? JSON.parse(value) as Usuario : null;
  }
}
