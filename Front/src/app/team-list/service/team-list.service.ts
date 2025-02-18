import { Injectable } from '@angular/core';
import { HttpClient,HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap,map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TeamListService {

  constructor(private http: HttpClient) { }

  getTeams(): Observable<any[]> {
    return this.http.get<any[]>(`/api/team`);
  }
  searchTeams(name: string): Observable<any[]> {
    
    return this.http.get<any[]>(`/api/team/search?nom=${name}`);
  }
}
