
import { CommonModule } from '@angular/common';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { Component, ViewChild, AfterViewInit , OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserModalComponent } from '../user-modal/user-modal.component';
import { ReactiveFormsModule,FormsModule,FormGroup,FormBuilder } from '@angular/forms';
import { UserListService } from './service/user-list.service';
import { HttpClientModule } from '@angular/common/http';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

const FILTER_PAG_REGEX = /[^0-9]/g;

@Component({
  selector: 'app-user-list',
  standalone: true,
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
  imports: [CommonModule,NgbPaginationModule,UserModalComponent,FormsModule,HttpClientModule,ReactiveFormsModule],
  providers: [UserListService]
})
export class UserListComponent implements AfterViewInit,OnInit {
  isVisible: boolean = false;
  users = [
    { id:0, firstName: 'Elizabeth',lastName: 'Lopez', username: 'elizabethtlopez', email: 'elopez@yahoo.com', role: 'Admin', status: true, civility: 'Mme.'
      
     },{id:1, firstName: 'mehdi',lastName: 'mehdi', username: 'mehdi', email: 'mehdi@yahoo.com', role: 'Admin', status: true, civility: 'Mr.'}
    // Add more users 
  ];
  searchForm: FormGroup;

  editUser(user: any) {
    // Handle edit user action
    console.log('Edit user:', user);
  }

  deleteUser(user: any) {
    // Handle delete user action
    console.log('Delete user:', user);
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


  @ViewChild(UserModalComponent) userModal!: UserModalComponent;



  constructor(private modalService: NgbModal,private userListService :UserListService,private fb: FormBuilder) {
    this.searchForm = this.fb.group({
    username: ['']
  });
}

ngOnInit(): void {
  this.loadUsers();

  this.searchForm.get('username')!.valueChanges
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
          const index = this.users.findIndex(user => user.username === newUser.username);
          if (index !== -1) {
            this.users[index] = newUser;
          }
        } else {
          //newUser.id = this.users.length + 1; // Assign a new ID or handle ID generation differently
          this.users.push(newUser);
        }
      });
    } else {
      console.error('UserModalComponent is not initialized');
    }
  }
  loadUsers() {
    this.userListService.getUsers().subscribe(
      (data: any[]) => {
        this.users = data;
      },
      error => {
        console.error('Error fetching users', error);
      }
    );
  }
  searchUsers(username: string) {
    if (username.trim()) {
      this.userListService.searchUsers(username).subscribe(
        (data: any[]) => {
          this.users = data;
        },
        error => {
          console.error('Error searching users', error);
        }
      );
    } else {
      this.loadUsers(); // If search input is cleared, load all users
    }
  }
  openCreateUserModal() {
    if (this.userModal) {
      this.userModal.isEditMode = false;
      this.userModal.user = null;
      this.userModal.openModal();
      
      /*this.userModal.userAdded.subscribe((newUser: any) => {
        this.users.push(newUser);
        console.log(this.users);
        
      });*/
      
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
      console.error('UserModalComponent is not initialized');
    }
  }

}
