import { Injectable } from '@angular/core';
import { HttpClient,HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap,map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BackListService {

  constructor(private http: HttpClient) { }

  getUsers(): Observable<any[]> {
    return this.http.get<any[]>(`/api/backlog`);
  }
  searchUsers(username: string): Observable<any[]> {

    return this.http.get<any[]>(`/api/backlog/search?titre=${username}`);
  }

  deleteBacklog(id: number): Observable<any[]>{
    return this.http.delete<any[]>(`/api/backlog/${id}`);
  }
}
