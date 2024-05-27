import { Injectable } from '@angular/core';
import { HttpClient,HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap,map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProjectListService {

  constructor(private http: HttpClient) { }

  getProjects(): Observable<any[]> {
    return this.http.get<any[]>(`/api/project`);
  }

  searchProjects(name: string): Observable<any[]> {

    return this.http.get<any[]>(`/api/project/search?name=${name}`);
  }
}
