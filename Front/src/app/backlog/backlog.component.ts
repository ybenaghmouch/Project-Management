import { Component, OnInit } from '@angular/core';
import { FilterByStatutPipe } from './filter-by-statut.pipe';
import { FormsModule } from '@angular/forms'; // If you need forms support
import { HttpClientModule } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common'; // Add this import

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
@Component({
  selector: 'app-backlog',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule ,FilterByStatutPipe],
  templateUrl: './backlog.component.html',
  styleUrl: './backlog.component.css'
})
export class BacklogComponent implements OnInit {
  backlog: any;
  backlogId: string | null = null;

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      this.backlogId = params.get('backlogId');
      if (this.backlogId) {
        this.getBacklog(this.backlogId);
      }
    });
  }

  getBacklog(id: string): void {
    // Replace with your API endpoint
    this.http.get<any[]>(`/api/backlog/search?titre=${id}`).subscribe(data => {
      if (data.length > 0) {
        this.backlog = data[0]; //to change change path
      }
    });
  }

  getPriorityColor(priority: number): string {
    if (priority <= 2) return 'green';
    if (priority <= 4) return 'blue';
    if (priority <= 6) return 'orange';
    return 'red';
  }
}