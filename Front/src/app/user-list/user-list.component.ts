
import { CommonModule } from '@angular/common';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { Component, ViewChild, AfterViewInit , OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserModalComponent } from '../user-modal/user-modal.component';
import { FormsModule } from '@angular/forms';


const FILTER_PAG_REGEX = /[^0-9]/g;

@Component({
  selector: 'app-user-list',
  standalone: true,
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
  imports: [CommonModule,NgbPaginationModule,UserModalComponent,FormsModule]
})
export class UserListComponent implements AfterViewInit {
  isVisible: boolean = false;
  users = [
    { id:2, firstName: 'Elizabeth',lastName: 'Lopez', username: 'elizabethtlopez', email: 'elopez@yahoo.com', role: 'Admin', status: true }
    // Add more users 
  ];

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



  constructor(private modalService: NgbModal) {}


  ngAfterViewInit() {
    if (this.userModal && !this.isSubscribed) {
      this.userModal.userAdded.subscribe((newUser: any) => {
        this.users.push(newUser);
        console.log("fff"+newUser);
      });
      this.isSubscribed = true;
    } else if (!this.userModal) {
      console.error('UserModalComponent is not initialized');
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
