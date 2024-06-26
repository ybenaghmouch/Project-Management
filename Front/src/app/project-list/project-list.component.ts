import { Component, ViewChild, AfterViewInit , OnInit, viewChild, ChangeDetectorRef} from '@angular/core';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule,FormsModule,FormGroup,FormBuilder } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ProjectModalComponent } from '../project-modal/project-modal.component';
import { ProjectListService } from './service/project-list.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { DatePipe } from '@angular/common';
import { RouterModule } from '@angular/router';

const FILTER_PAG_REGEX = /[^0-9]/g;

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [CommonModule,NgbPaginationModule,ProjectModalComponent,FormsModule,HttpClientModule,ReactiveFormsModule,RouterModule],
  providers: [ProjectListService, DatePipe],
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.css'
})
export class ProjectListComponent implements AfterViewInit,OnInit{
  isVisible: boolean = false;
  i:number=0;
  projects = [
    {
      "id": 0,
      "nom": "string",
      "description": "string",
      "dateDebut": "2024-06-22T20:14:58.316Z",
      "dateFin": "2024-06-22T20:14:58.316Z",
      "duree": 0,
      "status": "string",
      "manager": {
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
        "status": true,
        "password": "string",
        "firstName": "string",
        "civility": "string",
        "speciality": "string",
        "lastName": "string",
        "email": "string",
        "force_change_password": true
      },
      "backlogs": [
        {
          "id": 0,
          "titre": "string",
          "description": "string",
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
                    "status": true,
                    "password": "string",
                    "firstName": "string",
                    "civility": "string",
                    "speciality": "string",
                    "lastName": "string",
                    "email": "string",
                    "force_change_password": true
                  },
                  "statut": "string"
                }
              ],
              "statut": "string"
            }
          ],
          "status": "string"
        }
      ],
      "sprints": [
        {
          "id": 0,
          "titre": "string",
          "description": "string",
          "date_debut": "2024-06-22T20:14:58.316Z",
          "date_fin": "2024-06-22T20:14:58.316Z",
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
                  "responsable": {
                    "id": 0,
                    "username": "string",
                  },
                  "statut": "string"
                }
              ],
              "statut": "string"
            }
          ]
        }
      ],
      "equipe": {
        "id": 0,
        "nom": "string",
        "manager": {
          "id": 0,
          "username": "string",
          "soldeConge": 0,
        },
        "chefprojet": {
          "id": 0,
          "username": "string",

        },
        "collaborateurs": [
          {
            "id": 0,
            "username": "string",
          }
        ]
      }
    }
  ];

  searchForm: FormGroup;

  editProject(project: any){
    console.log('Edit project:', project);
  }

  deleteProject(project: any) {
    // Handle delete project action
    console.log('Delete project:', project);
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
  @ViewChild('projectModal', { static: false }) projectModal!: ProjectModalComponent;
  //@ViewChild(ProjectListComponent) projectModal!: ProjectModalComponent;
  //@ViewChild('projectModal', { static: false }) projectModal!: ProjectModalComponent;


  constructor(private modalService: NgbModal,private projectListService :ProjectListService,private fb: FormBuilder,
    private datePipe: DatePipe, private cd: ChangeDetectorRef) {
    this.searchForm = this.fb.group({
    name: ['']
  });
}

ngOnInit(): void {
  this.loadProjects();

  this.searchForm.get('name')!.valueChanges
    .pipe(
      debounceTime(300),
      distinctUntilChanged()
    )
    .subscribe(value => {
      this.searchProjects(value);
    });
}


transformDate(date: string | null): string | null {
  if (date === null) {
    return null;
  }
  return this.datePipe.transform(date, 'yyyy-MM-dd');
}


// ngAfterViewInit() {
//   if (this.projectModal) {
//     this.projectModal.projectAdded.subscribe((newProject: any) => {
//       if (this.projectModal.isEditMode) {
//         const index = this.projects.findIndex(project => project.nom === newProject.nom);
//         if (index !== -1) {
//           this.projects[index] = newProject;
//         }
//       } else {
//         //newProject.id = this.projects.length + 1; // Assign a new ID or handle ID generation differently
//         this.projects.push(newProject);
//       }
//     });
//   } else {
//     console.error('ProjectModalComponent is not initialized');
//   }
// }

ngAfterViewInit() {
  if (this.projectModal) {
    this.projectModal.projectAdded.subscribe((newProject: any) => {
      if (this.projectModal.isEditMode) {
        const index = this.projects.findIndex(project => project.nom === newProject.nom);
        if (index !== -1) {
          this.projects[index] = newProject;
        }
      } else {
        //newTeam.id = this.teams.length + 1; // Assign a new ID or handle ID generation differently
        this.projects.push(newProject);
      }
    });
  } else {
    console.error('ProjectModalComponent is not initialized');
  }
}


loadProjects() {
  this.projectListService.getProjects().subscribe(
    (data: any[]) => {
      this.projects = data;
    },
    error => {
      console.error('Error fetching projects', error);
    }
  );
}

searchProjects(projectname: string) {
  if (projectname.trim()) {
    this.projectListService.searchProjects(projectname).subscribe(
      (data: any[]) => {
        this.projects = data;
      },
      error => {
        console.error('Error searching projects', error);
      }
    );
  } else {
    this.loadProjects();
  }
}

openCreateProjectModal() {
  if (this.projectModal) {
    this.projectModal.isEditMode = false;
    this.projectModal.project = null;
    console.log("test1");
    this.projectModal.openModal();
  } else {
    console.error('ProjectModalComponent is not initialized');
    console.log("test2");
  }
}

openEditProjectModal(project: any) {
  if (this.projectModal) {
    this.projectModal.isEditMode = true;
    this.projectModal.project = project;
    this.projectModal.openModal();
  } else {
    console.error('ProjectModalComponent is not initialized');
  }
}

openListProjectModal(project: any) {
  if (this.projectModal) {
    this.projectModal.isListMode = true;
    this.projectModal.project = project;
    this.projectModal.openModal();
    this.projectModal.form.disable();
  } else {
    console.error('ProjectModalComponent is not initialized');
  }
}

}


