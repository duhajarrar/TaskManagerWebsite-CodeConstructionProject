import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../../models/user.model';
import { Gender, Role } from '../../constants/enum/user.enum';
import { Observable } from 'rxjs';
import { StorageService } from '../storage/storage.service';
import { environment } from 'src/environments/environment';

const AUTH_API = environment.apiUrl + 'auth/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private storage: StorageService) {}

  apiurl = 'http://localhost:3000/user';

  login(username: string, password: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'signin',
      {
        username,
        password,
      },
      httpOptions
    );
  }

  register(username: string, email: string, password: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'signup',
      {
        username,
        email,
        password,
      },
      httpOptions
    );
  }

  refreshToken(token: string) {
    return this.http.post(
      AUTH_API + 'refreshtoken',
      {
        refreshToken: token,
      },
      httpOptions
    );
  }

  logout(): Observable<any> {
    return this.http.post(AUTH_API + 'signout', {}, httpOptions);
  }
}
