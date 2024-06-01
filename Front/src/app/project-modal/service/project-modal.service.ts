import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectModalService {

  constructor(private http: HttpClient) { }


  postProject(formData: any): Observable<any> {
    return this.http.post<any>(`/api/project`,formData);
  }
  putProject(formData: any,name: string): Observable<any> {
    return this.http.put<any>(`/api/project/${name}`,formData);
  }
  getUsers(type: string = "user"): Observable<any[]> {
    return this.http.get<any[]>(`/api/user`);
  }

}
