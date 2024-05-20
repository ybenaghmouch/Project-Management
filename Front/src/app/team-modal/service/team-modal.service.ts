import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TeamModalService {

  constructor(private http: HttpClient) { }

  postTeam(formData: any): Observable<any> {
    return this.http.post<any>(`/api/team`,formData);
  }
  putTeam(formData: any,username: string): Observable<any> {
    return this.http.put<any>(`/api/team/${username}`,formData);
  }
  getUsers(type: string = "user"): Observable<any[]> {
    return this.http.get<any[]>(`/api/user`);
  }
}
