import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskListService {

  constructor(private http: HttpClient) {}

  getBacklog(id: string): Observable<any[]> {
    return this.http.get<any[]>(`/api/us/${id}`);
  }
}
