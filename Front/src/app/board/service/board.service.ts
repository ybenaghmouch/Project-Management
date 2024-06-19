import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

@Injectable({
  providedIn: 'root'
})
export class BoardService {

  constructor(private http: HttpClient) {}

  getUserStories(): Observable<any[]> {
    return this.http.get<any[]>(`/api/us`);
  }
  updateUserStory(us:any): Observable<any[]> {
    return this.http.put<any[]>(`/api/us/${us.code}`,us);
  }
  updateFeatureStatus(feature: any): Observable<any> {
    return this.http.put(`/api/tache/${feature.code}`, feature);
  }
}
