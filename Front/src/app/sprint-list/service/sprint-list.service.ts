import { Injectable } from '@angular/core';
import { HttpClient,HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap,map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SprintListService {

  constructor(private http: HttpClient) { }
  getSprints(titre: string): Observable<any[]> {
    return this.http.get<any[]>(`/api/project/sprints/${titre}`);
  }
  // getAllSprint(): Observable<any[]> {
  //   return this.http.get<any[]>('/api/sprint');
  // }

  searchSprints(titre: string): Observable<any[]> {

    return this.http.get<any[]>(`/api/sprint/search?titre=${titre}`);
  }
}
