import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Task } from '../../models/task.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  constructor(private http: HttpClient) {}
  apiurl = 'api/task';

  AddTask(task: Task): Observable<Task> {
    return this.http.post<Task>(`${this.apiurl}/create`, task);
  }

  updateTask(existingTask: Task): Observable<Task> {
    return this.http.put<Task>(
      `${this.apiurl}/update/${existingTask.taskId}`,
      existingTask
    );
  }

  GetTaskById(taskId: string): Observable<Task> {
    return this.http.get<Task>(`${this.apiurl}/getTask/${taskId}`);
  }

  DeleteTask(taskId: string) {
    console.log('Get task by id');
    return this.http.delete(`${this.apiurl}/delete/${taskId}`);
  }

  GetAllTasks() {
    return this.http.get(`${this.apiurl}/all`);
  }
}
