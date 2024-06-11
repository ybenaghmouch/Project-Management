import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SprintModalService {

  constructor(private http: HttpClient) { }
  postSprint(formData: any): Observable<any> {
    return this.http.post<any>(`/api/sprint`,formData);
  }
  putSprint(formData: any,titre: string): Observable<any> {
    return this.http.put<any>(`/api/sprint/${titre}`,formData);
  }
  getUserStories(type: string = "userStories"): Observable<any[]> {
    return this.http.get<any[]>(`/api/us`);
  }
}
