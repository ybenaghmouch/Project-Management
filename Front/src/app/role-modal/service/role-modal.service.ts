import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleModalService {

  constructor(private http: HttpClient) { }

  postRole(formData: any): Observable<any> {
    return this.http.post<any>(`/api/role`,formData);
  }
  putRole(formData: any,username: string): Observable<any> {
    return this.http.put<any>(`/api/role/${username}`,formData);
  }
  getUsers(type: string = "user"): Observable<any[]> {
    return this.http.get<any[]>(`/api/user`);
  }
}
