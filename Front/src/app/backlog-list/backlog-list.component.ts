import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectListService } from '../project-list/service/project-list.service';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { Component, ViewChild, AfterViewInit, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BacklogModalComponent } from '../backlog-modal/backlog-modal.component';
import { ReactiveFormsModule, FormsModule, FormGroup, FormBuilder } from '@angular/forms';
import { BackListService } from './service/back-list.service';
import { HttpClientModule } from '@angular/common/http';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { RouterModule } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
const FILTER_PAG_REGEX = /[^0-9]/g;

@Component({
  selector: 'app-backlog-list',
  standalone: true,
  imports: [CommonModule, NgbPaginationModule, BacklogModalComponent, FormsModule, HttpClientModule, ReactiveFormsModule, RouterModule, NgbDropdownModule],
  providers: [BackListService, ProjectListService],
  templateUrl: './backlog-list.component.html',
  styleUrl: './backlog-list.component.css'
})
export class BacklogListComponent implements AfterViewInit, OnInit {
  isVisible: boolean = false;
  users = [
    {
      "id": -1,
      "titre": "",
      "description": "",
      "status": ""
    }
    // Add more users
  ];
  projects: any[] = [];
  searchForm: FormGroup;
  page = 1;

  @ViewChild(BacklogModalComponent) userModal!: BacklogModalComponent;

  constructor(
    private route: ActivatedRoute,
    private modalService: NgbModal,
    private userListService: BackListService,
    private fb: FormBuilder,
    private projectListService: ProjectListService,
    private router: Router
  ) {
    this.searchForm = this.fb.group({
      titre: ['']
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const teamName = params['projectname'];
      if (teamName) {
        this.projectListService.getProjects().subscribe(
          (data: any[]) => {
            this.projects = data;

          },
          error => {
            console.error('Error fetching projects', error);
          }
        );
        this.loadUsers(teamName);
      } else {
        this.projectListService.getProjects().subscribe(
          (data: any[]) => {
            this.projects = data;
            const teamName = data[0]?.nom;
            if (teamName) {
              this.loadUsers(teamName);
            }
          },
          error => {
            console.error('Error fetching projects', error);
          }
        );

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

  ngAfterViewInit() {
    if (this.userModal) {
      this.userModal.userAdded.subscribe((newUser: any) => {
        if (this.userModal.isEditMode) {
          const index = this.users.findIndex(user => user.titre === newUser.titre);
          if (index !== -1) {
            this.users[index] = newUser;
          }
        } else {
          this.users.push(newUser);
        }
      });
    } else {
      console.error('UserModalComponent is not initialized');
    }
  }

  loadUsers(projectname: string = "debug") {
    console.log(projectname);
    this.userListService.getUsers(projectname).subscribe(
      (data: any[]) => {
        this.users = data;
      },
      error => {
        console.error('Error fetching users', error);
      }
    );
  }

  searchUsers(titre: string) {
    if (titre.trim()) {
      this.userListService.searchUsers(titre).subscribe(
        (data: any[]) => {
          this.users = data;
        },
        error => {
          console.error('Error searching users', error);
        }
      );
    } else {
      this.route.params.subscribe(params => {
        const teamName = params['projectname'];
        if (teamName) {
          this.loadUsers(teamName);
        }
      }); // If search input is cleared, load all users
    }
  }

  openCreateUserModal() {
    if (this.userModal) {
      this.userModal.isEditMode = false;
      this.route.params.subscribe(params => {
        const teamName = params['projectname'];
        this.userModal.projectname = teamName;
      });

      this.userModal.user = null;
      this.userModal.openModal();
    } else {
      console.error('UserModalComponent is not initialized');
    }
  }

  openEditUserModal(user: any) {
    if (this.userModal) {
      this.userModal.isEditMode = true;
      this.userModal.user = user;
      this.userModal.openModal();
    } else {
      console.error('BacklogMdalComponent is not initialized');
    }
  }

  openListUserModal(user: any) {
    if (this.userModal) {
      this.userModal.isListMode = true;
      this.userModal.user = user;
      this.userModal.openModal();
      this.userModal.form.disable();
    } else {
      console.error('BacklogMdalComponent is not initialized');
    }
  }


  onProjectChange(event: any) {
    const projectName = event.target.value;
    this.router.navigate(['/backlogs', projectName]);
    this.loadUsers(projectName);
  }

  editUser(user: any) {
    console.log('Edit user:', user);
  }

  deleteBacklog(user: any) {
    const index: number = this.users.indexOf(user);
    this.userListService.deleteBacklog(user.id).subscribe(() => {
      if (index !== -1) {
        this.users.splice(index, 1);
      }
    });
    console.log('Delete backlog:', user);

  }

//   deleteBacklog(user: any) {
//     const confirmation = window.confirm("Êtes-vous sûr de vouloir supprimer ce backlog ?");
//     if (confirmation) {
//         const index: number = this.users.indexOf(user);
//         this.userListService.deleteBacklog(user.id).subscribe(() => {
//           if (index !== -1) {
//             this.users.splice(index, 1);
//           }
//           console.log('Backlog supprimé:', user);
//           alert('Le backlog a été supprimé avec succès.');
//         });
//     } else {
//         console.log('Suppression annulée:', user);
//     }
// }


  getPageSymbol(current: number) {
    return ['A', 'B', 'C', 'D', 'E', 'F', 'G'][current - 1];
  }

  selectPage(page: string) {
    this.page = parseInt(page, 10) || 1;
  }

  formatInput(input: HTMLInputElement) {
    input.value = input.value.replace(FILTER_PAG_REGEX, '');
  }
}
