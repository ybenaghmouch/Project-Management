import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleListService {

  constructor(private http: HttpClient) { }

  getRoles(): Observable<any[]>{
    return this.http.get<any[]>(`/api/role`);
  }


}
