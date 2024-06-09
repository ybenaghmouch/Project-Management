import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsModalService {

  constructor(private http: HttpClient) { }

  postUsers(formData: any,projectname: string): Observable<any> {
    return this.http.post<any>(`/api/backlog/${projectname}`,formData);
  }
  putUsers(formData: any,username: string): Observable<any> {
    return this.http.put<any>(`/api/backlog/${username}`,formData);
  }

}
