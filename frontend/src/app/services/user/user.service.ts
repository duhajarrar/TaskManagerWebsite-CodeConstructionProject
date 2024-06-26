import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Task } from 'src/app/models/task.model';
import { environment } from 'src/environments/environment';

const USER_API = environment.apiUrl + 'user/';
const TASK_API = environment.apiUrl + 'task/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  GetAll() {
    return this.http.get(USER_API);
  }

  GetByCode(code: any) {
    return this.http.get(USER_API + '/' + code);
  }

  UpdateUser(code: any, postData: any) {
    return this.http.put(USER_API + '/' + code, postData, httpOptions);
  }

  GetUserTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(TASK_API + '/tasks');
  }

  GetAllUsers(): any {
    return this.http.get(USER_API + 'user/members'); // why not users in db.json??
  }
}
