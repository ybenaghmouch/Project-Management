import { CommonModule } from '@angular/common';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { Component, ViewChild, AfterViewInit , OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TeamModalComponent } from '../team-modal/team-modal.component';
import { ReactiveFormsModule,FormsModule,FormGroup,FormBuilder } from '@angular/forms';
import { TeamListService } from './service/team-list.service';
import { HttpClientModule } from '@angular/common/http';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { RouterModule } from '@angular/router';
const FILTER_PAG_REGEX = /[^0-9]/g;

@Component({
  selector: 'app-team-list',
  standalone: true,
  imports: [CommonModule,NgbPaginationModule,TeamModalComponent,FormsModule,HttpClientModule,ReactiveFormsModule,RouterModule],
  providers: [TeamListService],
  templateUrl: './team-list.component.html',
  styleUrl: './team-list.component.css'
})
export class TeamListComponent implements AfterViewInit,OnInit{
  isVisible: boolean = false;
  teams = [
    {
      "id": 0,
      "manager": {
        "id": 0,
        "username": "string",
        "role": "string",
        "status": true,
        "speciality": "string",
        "lastName": "string",
        "firstName": "string",
        "email": "string",
        "civility": "string",
        "force_change_password": true,
        "password": "string"
      },
      "nom": "string",
      "collaborateurs": [
        {
          "id": 0,
          "username": "string",
          "soldeConge": 0,
          "role": "string",
          "status": true,
          "speciality": "string",
          "lastName": "string",
          "firstName": "string",
          "email": "string",
          "civility": "string",
          "force_change_password": true,
          "password": "string"
        }
      ],
      "chefprojet": {
        "id": 0,
        "username": "string",
        "soldeConge": 0,
        "role": "string",
        "status": true,
        "speciality": "string",
        "lastName": "string",
        "firstName": "string",
        "email": "string",
        "civility": "string",
        "force_change_password": true,
        "password": "string"
      }
    }
    // Add more teams 
  ];
  searchForm: FormGroup;

  editTeam(team: any) {
    // Handle edit team action
    console.log('Edit team:', team);
  }

  deleteTeam(team: any) {
    // Handle delete team action
    console.log('Delete team:', team);
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


  @ViewChild(TeamModalComponent) teamModal!: TeamModalComponent;



  constructor(private modalService: NgbModal,private teamListService :TeamListService,private fb: FormBuilder) {
    this.searchForm = this.fb.group({
    name: ['']
  });
}

ngOnInit(): void {
  this.loadTeams();

  this.searchForm.get('name')!.valueChanges
    .pipe(
      debounceTime(300), // Wait for 300ms pause in events
      distinctUntilChanged() // Only emit if value is different from previous value
    )
    .subscribe(value => {
      this.searchTeams(value);
    });
}
  ngAfterViewInit() {
    if (this.teamModal) {
      this.teamModal.teamAdded.subscribe((newTeam: any) => {
        if (this.teamModal.isEditMode) {
          const index = this.teams.findIndex(team => team.nom === newTeam.nom);
          if (index !== -1) {
            this.teams[index] = newTeam;
          }
        } else {
          //newTeam.id = this.teams.length + 1; // Assign a new ID or handle ID generation differently
          this.teams.push(newTeam);
        }
      });
    } else {
      console.error('TeamModalComponent is not initialized');
    }
  }
  loadTeams() {
    this.teamListService.getTeams().subscribe(
      (data: any[]) => {
        this.teams = data;
      },
      error => {
        console.error('Error fetching teams', error);
      }
    );
  }
  searchTeams(teamname: string) {
    if (teamname.trim()) {
      this.teamListService.searchTeams(teamname).subscribe(
        (data: any[]) => {
          this.teams = data;
        },
        error => {
          console.error('Error searching teams', error);
        }
      );
    } else {
      this.loadTeams(); // If search input is cleared, load all teams
    }
  }
  openCreateTeamModal() {
    
    if (this.teamModal) {
      
      this.teamModal.isEditMode = false;
      this.teamModal.team = null;
      console.log("test1");
      this.teamModal.openModal();
      
      
      
    } else {
      console.error('TeamModalComponent is not initialized');
      console.log("test2");
    }
  }

  openEditTeamModal(team: any) {
    if (this.teamModal) {
      this.teamModal.isEditMode = true;
      this.teamModal.team = team;
      
      this.teamModal.openModal();
    } else {
      console.error('TeamModalComponent is not initialized');
    }
  }

}
