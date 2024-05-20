import { Injectable } from '@angular/core';
import { HttpClient,HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap,map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class UserListService {

  constructor(private http: HttpClient) { }

  getUsers(): Observable<any[]> {
    return this.http.get<any[]>(`/api/user`);
  }
  searchUsers(username: string): Observable<any[]> {
    
    return this.http.get<any[]>(`/api/user/search?username=${username}`);
  }
}