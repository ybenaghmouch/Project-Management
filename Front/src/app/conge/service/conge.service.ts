import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CongeService {
  private apiUrl = '/api/conges'; // Replace with your actual API URL

  constructor(private http: HttpClient) {}

  getAllConges(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  createConge(conge: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, conge);
  }

  updateConge(id: number, conge: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, conge);
  }
}
