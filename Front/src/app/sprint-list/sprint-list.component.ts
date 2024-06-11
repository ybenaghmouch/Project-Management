import { Component, ViewChild, AfterViewInit , OnInit, viewChild, ChangeDetectorRef } from '@angular/core';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule,FormsModule,FormGroup,FormBuilder } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { SprintModalComponent } from '../sprint-modal/sprint-modal.component';
import { SprintListService } from './service/sprint-list.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { DatePipe } from '@angular/common';
const FILTER_PAG_REGEX = /[^0-9]/g;

@Component({
  selector: 'app-sprint-list',
  standalone: true,
  imports: [CommonModule,NgbPaginationModule,SprintModalComponent,FormsModule,HttpClientModule,ReactiveFormsModule],
  providers:[SprintListService, DatePipe],
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

  constructor(private modalService: NgbModal,private sprintListService :SprintListService,private fb: FormBuilder,
    private datePipe: DatePipe, private cd: ChangeDetectorRef) {
    this.searchForm = this.fb.group({
    titre: ['']
  });
}

ngOnInit(): void {
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


loadSprints() {
  this.sprintListService.getSprints().subscribe(
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
    this.loadSprints();
  }
}



openCreateSprintModal() {
  if (this.sprintModal) {
    this.sprintModal.isEditMode = false;
    this.sprintModal.sprint = null;
    console.log("test1");
    this.sprintModal.openModal();
  } else {
    console.error('SprintModalComponent is not initialized');
    console.log("test2");
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
