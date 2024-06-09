import { Component, OnInit } from '@angular/core';
import { FilterByStatutPipe } from './filter-by-statut.pipe';
import { ReactiveFormsModule,FormsModule,FormGroup,FormBuilder } from '@angular/forms';import { HttpClientModule } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common'; // Add this import
import { BacklogService } from './service/backlog.service';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
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
  imports: [CommonModule, HttpClientModule ,FilterByStatutPipe,FormsModule,HttpClientModule,ReactiveFormsModule],
  providers:[BacklogService],
  templateUrl: './backlog.component.html',
  styleUrl: './backlog.component.css'
})
export class BacklogComponent implements OnInit {
  backlog: any;
  backlogId: string | null = null;

  constructor(private route: ActivatedRoute, private http: HttpClient,private backlogService:BacklogService,private fb: FormBuilder) {
    this.searchForm = this.fb.group({
    titre: ['']
  });

}
  searchForm: FormGroup;
  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      this.backlogId = params.get('backlogId');
      if (this.backlogId) {
        this.getBacklog(this.backlogId);
      }
    });


    this.searchForm.get('titre')!.valueChanges
    .pipe(
      debounceTime(300), // Wait for 300ms pause in events
      distinctUntilChanged() // Only emit if value is different from previous value
    )
    .subscribe(value => {
      this.searchUsers(value);
    });
  }

  getBacklog(id: string): void {
    // Replace with your API endpoint
    this.backlogService.getBacklog(id).subscribe(data => {
      
        this.backlog = data; //to change change path
      
    });
  }
  searchUsers(titre: string) {
   
  }

  getPriorityColor(priority: number): string {
    if (priority <= 2) return 'green';
    if (priority <= 4) return 'blue';
    if (priority <= 6) return 'orange';
    return 'red';
  }
}