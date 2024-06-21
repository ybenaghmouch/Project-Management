import { Component, ViewChild, AfterViewInit , OnInit, viewChild, ChangeDetectorRef } from '@angular/core';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { ProjectListService } from '../project-list/service/project-list.service';
import { ReactiveFormsModule,FormsModule,FormGroup,FormBuilder } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { SprintModalComponent } from '../sprint-modal/sprint-modal.component';
import { SprintListService } from './service/sprint-list.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
const FILTER_PAG_REGEX = /[^0-9]/g;

@Component({
  selector: 'app-sprint-list',
  standalone: true,
  imports: [CommonModule,NgbPaginationModule,SprintModalComponent,FormsModule,HttpClientModule,ReactiveFormsModule],
  providers:[SprintListService, DatePipe, ProjectListService],
  templateUrl: './sprint-list.component.html',
  styleUrl: './sprint-list.component.css'
})
export class SprintListComponent implements AfterViewInit, OnInit{
  isVisible: boolean = false;
  sprints = [
    {
      "titre": "string",
      "description": "string",
      "date_debut": "2024-06-09T00:25:48.568Z",
      "date_fin": "2024-06-09T00:25:48.568Z",
      "status": "string",
      "userStories": [
        {
          "code": "string",
          "titre": "string",
          "priority": 0,
          "id": 0,
          "description": "string",
          "features": [
            {
              "code": "string",
              "titre": "string",
              "priority": 0,
              "id": 0,
              "description": "string",
              "statut": "string",
              "responsable": {
                "id": 0,
                "username": "string",
                "soldeConge": 0,
                "authorities": [
                  {
                    "id": 0,
                    "authority": "string",
                    "authorities": [
                      {
                        "id": 0,
                        "authority": "string"
                      }
                    ]
                  }
                ],
                "password": "string",
                "status": true,
                "lastName": "string",
                "firstName": "string",
                "email": "string",
                "civility": "string",
                "speciality": "string",
                "force_change_password": true
              }
            }
          ],
          "statut": "string"
        }
      ]
    }
  ];
  projects: any[] = [];

  searchForm: FormGroup;

  editSprint(sprint: any){
    console.log('Edit sprint:', sprint);
  }

  deleteSprint(sprint: any) {
    // Handle delete sprint action
    console.log('Delete sprint:', sprint);
  }

  page = 1;
  private isSubscribed: boolean = false;

	getPageSymbol(current: number) {
		return ['A', 'B', 'C', 'D', 'E', 'F', 'G'][current - 1];
	}

	selectPage(page: string) {
		this.page = parseInt(page, 10) || 1;
	}

	formatInput(input: HTMLInputElement) {
		input.value = input.value.replace(FILTER_PAG_REGEX, '');
	}

  @ViewChild('sprintModal', { static: false }) sprintModal!: SprintModalComponent;
  //@ViewChild(SprintModalComponent) sprintModal!: SprintModalComponent;

  constructor(private router: Router, private projectListService: ProjectListService, private route: ActivatedRoute, private modalService: NgbModal,private sprintListService :SprintListService,private fb: FormBuilder,
    private datePipe: DatePipe, private cd: ChangeDetectorRef) {
    this.searchForm = this.fb.group({
    titre: ['']
  });
}

ngOnInit(): void {
  this.route.params.subscribe(params =>{
    const projectname = params['projectname'];
    if(projectname){
      this.projectListService.getProjects().subscribe(
        (data: any[])=>{
          this.projects = data;
        },
        error => {
        console.error('Error fetching projects', error);
        }
      );
      this.loadSprints(projectname);
    }else {
      this.projectListService.getProjects().subscribe(
        (data: any[])=>{
          this.projects = data;
          const projectname = data[0]?.nom;
          if(projectname){
            this.loadSprints(projectname);
          }
        },
        error => {
          console.error('Error fetching projects', error);
        }
      );
    }
  })
  this.loadSprints();

  this.searchForm.get('titre')!.valueChanges
    .pipe(
      debounceTime(300),
      distinctUntilChanged()
    )
    .subscribe(value => {
      this.searchSprints(value);
    });
}
transformDate(date: string | null): string | null {
  if (date === null) {
    return null;
  }
  return this.datePipe.transform(date, 'yyyy-MM-dd');
}

ngAfterViewInit() {
  if (this.sprintModal) {
    this.sprintModal.sprintAdded.subscribe((newSprint: any) => {
      if (this.sprintModal.isEditMode) {
        const index = this.sprints.findIndex(sprint => sprint.titre === newSprint.titre);
        if (index !== -1) {
          this.sprints[index] = newSprint;
        }
      } else {
        this.sprints.push(newSprint);
      }
    });
  } else {
    console.error('SprintModalComponent is not initialized');
  }
}


loadSprints(projectname : string = "debug") {
  console.log(projectname);
  this.sprintListService.getSprints(projectname).subscribe(
    (data: any[]) => {
      this.sprints = data;
    },
    error => {
      console.error('Error fetching sprints', error);
    }
  );
}

searchSprints(sprintname: string) {
  if (sprintname.trim()) {
    this.sprintListService.searchSprints(sprintname).subscribe(
      (data: any[]) => {
        this.sprints = data;
      },
      error => {
        console.error('Error searching sprints', error);
      }
    );
  } else {
    this.route.params.subscribe(params => {
      const teamName = params['projectname'];
      if (teamName) {
        this.loadSprints(teamName);
      }
    });
  }
}

onProjectChange(event: any) {
  const projectName = event.target.value;
  this.router.navigate(['/sprints', projectName]);
  this.loadSprints(projectName);
}

openCreateSprintModal() {
  if (this.sprintModal) {
    this.sprintModal.isEditMode = false;
    this.route.params.subscribe(params =>{
      const te = params['projectname'];
      this.sprintModal.projectname = te;
    });
    this.sprintModal.sprint = null;
    this.sprintModal.openModal();
  } else {
    console.error('SprintModalComponent is not initialized');
  }
}

openEditSprintModal(sprint: any) {
  if (this.sprintModal) {
    this.sprintModal.isEditMode = true;
    this.sprintModal.sprint = sprint;
    this.sprintModal.openModal();
  } else {
    console.error('SprintModalComponent is not initialized');
  }
}

}
