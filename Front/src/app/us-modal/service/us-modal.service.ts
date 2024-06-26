import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsModalService {

  constructor(private http: HttpClient) { }

  postUsers(formData: any,backlogname: string): Observable<any> {
    return this.http.post<any>(`/api/us/${backlogname}/userstories`,formData);
  }
  putUsers(formData: any,username: string): Observable<any> {
    return this.http.put<any>(`/api/us/${username}`,formData);
  }

  getUsers(type: string = "user"): Observable<any[]> {
    return this.http.get<any[]>(`/api/user`);
  }

}
