import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TeamModalService {

  constructor(private http: HttpClient) { }

  postUsers(formData: any): Observable<any> {
    return this.http.post<any>(`/api/user`,formData);
  }
  putUsers(formData: any,username: string,role: string): Observable<any> {
    return this.http.put<any>(`/api/team/${username}`,formData);
  }
}
