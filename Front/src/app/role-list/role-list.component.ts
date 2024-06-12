import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { RoleModalComponent } from '../role-modal/role-modal.component';
import { ReactiveFormsModule,FormsModule,FormGroup,FormBuilder } from '@angular/forms';
import { RoleListService } from './service/role-list.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
const FILTER_PAG_REGEX = /[^0-9]/g;

@Component({
  selector: 'app-role-list',
  standalone: true,
  imports: [CommonModule, NgbPaginationModule, RoleModalComponent, FormsModule, HttpClientModule, ReactiveFormsModule, RouterModule, HttpClientModule, NgbDropdownModule],
  providers:[RoleListService],
  templateUrl: './role-list.component.html',
  styleUrl: './role-list.component.css'
})
export class RoleListComponent implements AfterViewInit, OnInit{
  roles = [
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
  ];
 /* searchForm: FormGroup;*/


  editRole(role: any) {
    console.log('Edit role:', role);
  }

  deleteRole(role: any) {
    console.log('Delete role:', role);
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


  @ViewChild(RoleModalComponent) roleModal!: RoleModalComponent;

  constructor(private modalService: NgbModal,private roleListService :RoleListService,/*private fb: FormBuilder*/ private http: HttpClient) {
   /* this.searchForm = this.fb.group({
    name: ['']
  });*/
}

ngOnInit(): void {
  this.loadRoles();

  // this.searchForm.get('name')!.valueChanges
  //   .pipe(
  //     debounceTime(300), // Wait for 300ms pause in events
  //     distinctUntilChanged() // Only emit if value is different from previous value
  //   )
  //   .subscribe(value => {
  //     this.searchRoles(value);
  //   });
}
  ngAfterViewInit() {
    if (this.roleModal) {
      this.roleModal.roleAdded.subscribe((newRole: any) => {
        if (this.roleModal.isEditMode) {
          const index = this.roles.findIndex(role => role.authority === newRole.authority);
          if (index !== -1) {
            this.roles[index] = newRole;
          }
        } else {
          //newRole.id = this.roles.length + 1; // Assign a new ID or handle ID generation differently
          this.roles.push(newRole);
        }
      });
    } else {
      console.error('RoleModalComponent is not initialized');
    }
  }

  loadRoles() {
    this.roleListService.getRoles().subscribe(
      (data: any[]) => {
        console.log('Data fetched:', data);
        this.roles = data;

      },
      error => {
        console.error('Error fetching roles', error);
      }
    );
  }


  // searchRoles(rolename: string) {
  //   if (rolename.trim()) {
  //     this.roleListService.searchRoles(rolename).subscribe(
  //       (data: any[]) => {
  //         this.roles = data;
  //       },
  //       error => {
  //         console.error('Error searching roles', error);
  //       }
  //     );
  //   } else {
  //     this.loadRoles(); // If search input is cleared, load all roles
  //   }
  // }
  openCreateRoleModal() {
    if (this.roleModal) {
      this.roleModal.isEditMode = false;
      this.roleModal.role = null;
      console.log("test1");
      this.roleModal.openModal();
    } else {
      console.error('RoleModalComponent is not initialized');
      console.log("test2");
    }
  }

  openEditRoleModal(role: any) {
    if (this.roleModal) {
      this.roleModal.isEditMode = true;
      this.roleModal.role = role;
      this.roleModal.openModal();
    } else {
      console.error('RoleModalComponent is not initialized');
    }
  }

  openListRoleModal(role: any) {
    if (this.roleModal) {
      this.roleModal.isListMode = true;
      this.roleModal.role = role;
      this.roleModal.openModal();
      this.roleModal.form.disable();
    } else {
      console.error('RoleModalComponent is not initialized');
    }
  }

}
