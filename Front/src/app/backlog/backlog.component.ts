import { Component, OnInit,ViewChild,ChangeDetectorRef,NgModule   } from '@angular/core';
import { FilterByStatutPipe } from './filter-by-statut.pipe';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';

import { ReactiveFormsModule,FormsModule,FormGroup,FormBuilder } from '@angular/forms';import { HttpClientModule } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbPaginationModule,NgbModule  } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common'; // Add this import
import { BacklogService } from './service/backlog.service';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { UsModalComponent } from '../us-modal/us-modal.component';
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
  imports: [CommonModule,NgbModule,HttpClientModule ,NgbPaginationModule,FilterByStatutPipe,FormsModule,HttpClientModule,ReactiveFormsModule,UsModalComponent,RouterModule],
  providers:[BacklogService],
  templateUrl: './backlog.component.html',
  styleUrl: './backlog.component.css'
})
export class BacklogComponent implements OnInit {
  backlog: any;
  backlogId: string | null = null;
  @ViewChild(UsModalComponent) userModal!: UsModalComponent;
  searchForm: FormGroup;
  filteredUserStories: any[] = [];
  page: number = 1;
  pageSize: number = 10;

  constructor(private cdr: ChangeDetectorRef, private route: ActivatedRoute, private backlogService: BacklogService, private fb: FormBuilder) {
    this.searchForm = this.fb.group({
      titre: [''],
      statut: ['']
    });
  }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      this.backlogId = params.get('backlogId');
      if (this.backlogId) {
        this.getBacklog(this.backlogId);
      }
    });

    this.searchForm.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged()
      )
      .subscribe(() => {
        this.filterUserStories();
      });
  }

  getBacklog(id: string): void {
    this.backlogService.getBacklog(id).subscribe(data => {
      this.backlog = data;
      this.filterUserStories();
    });
  }

  filterUserStories() {
    if (this.backlog) {
      const { titre, statut } = this.searchForm.value;
      this.filteredUserStories = this.backlog.userStories.filter((userStory: any) => {
        return (!titre || userStory.titre.toLowerCase().includes(titre.toLowerCase())) &&
               (!statut || userStory.statut === statut);
      });
    }
  }

  getPriorityColor(priority: number): string {
    if (priority <= 2) return 'green';
    if (priority <= 4) return 'blue';
    if (priority <= 6) return 'orange';
    return 'red';
  }

  openCreateUserModal() {
    if (this.userModal) {
      this.userModal.isEditMode = false;
      this.userModal.projectname = this.backlog.titre;
      this.userModal.user = null;
      this.userModal.openModal();

      this.userModal.userAdded.subscribe((newUser: any) => {
        this.backlog.userStories.push(newUser);
        this.filterUserStories();
        this.cdr.detectChanges();
      });
    } else {
      console.error('ModalComponent is not initialized');
    }
  }

  openEditUserModal(user: any) {
    if (this.userModal) {
      this.userModal.isEditMode = true;
      this.userModal.user = user;
      this.userModal.projectname = this.backlog.titre;
      this.userModal.openModal();

      this.userModal.userAdded.subscribe((updatedUser: any) => {
        const index = this.backlog.userStories.findIndex((us: any) => us.id === updatedUser.id);
        if (index !== -1) {
          this.backlog.userStories[index] = updatedUser;
        }
        this.filterUserStories();
        this.cdr.detectChanges();
      });
    } else {
      console.error('ModalComponent is not initialized');
    }
  }

  deleteUserStory(user: any) {
    const index = this.backlog.userStories.indexOf(user, 0);
    if (index > -1) {
      this.backlog.userStories.splice(index, 1);
      this.filterUserStories();
      this.cdr.detectChanges();
    }
  }

  openEditFeatureModal(feature: any) {
    // Logique pour ouvrir le modal d'édition pour la feature
    console.log('Open edit feature modal', feature);
  }

  deleteFeature(feature: any) {
    // Logique pour supprimer la feature
    console.log('Delete feature', feature);
  }

  setFilter(statut: string) {
    this.searchForm.patchValue({ statut });
  }
}
