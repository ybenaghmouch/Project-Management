import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
interface Feature {
  code: string;
  titre: string;
  description: string;
  id: number;
  statut: string;
  responsable: string | null;
  priority: number;
}

interface UserStory {
  code: string;
  titre: string;
  description: string;
  priority: number;
  id: number;
  features: Feature[];
  statut: string;
}

interface Backlog {
  id: number;
  titre: string;
  description: string;
  userStories: UserStory[];
  status: string;
}
@Injectable({
  providedIn: 'root'
})
export class BacklogService {
  



  constructor(private http: HttpClient) {}

  getBacklog(id: string): Observable<Backlog[]> {
    return this.http.get<Backlog[]>(`/api/backlog/${id}`);
  }
}
