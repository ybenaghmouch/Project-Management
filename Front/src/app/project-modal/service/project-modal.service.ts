import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectModalService {

  constructor(private http: HttpClient) { }

  submit(formData: any): Observable<any> {
    return this.http.post(`/api/project`, formData);
  }

  postProject(formData: any): Observable<any> {
    return this.http.post<any>(`/api/project`,formData);
  }
  putProject(formData: any,username: string): Observable<any> {
    return this.http.put<any>(`/api/project/${username}`,formData);
  }
  getUsers(type: string = "user"): Observable<any[]> {
    return this.http.get<any[]>(`/api/user`);
  }
}
