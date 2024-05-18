
import { CommonModule } from '@angular/common';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { Component, ViewChild, AfterViewInit } from '@angular/core';
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
  users = [
    { id:2, name: 'Elizabeth Lopez', username: 'elizabethtlopez', email: 'elopez@yahoo.com', role: 'Admin', status: 'Active' }
    // Add more users here
  ];

  editUser(user: any) {
    // Handle edit user action
    console.log('Edit user:', user);
  }

  deleteUser(user: any) {
    // Handle delete user action
    console.log('Delete user:', user);
  }

  page = 4;

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



  constructor() {}

  ngAfterViewInit() {
    // This ensures that the userModal is initialized after the view is initialized
    if (!this.userModal) {
      console.error('UserModalComponent is not initialized');
    }
  }

  openCreateUserModal() {
    if (this.userModal) {
      this.userModal.isEditMode = false;
      this.userModal.openModal();
    } else {
      console.error('UserModalComponent is not initialized');
    }
  }

  openEditUserModal(user: any) {
    if (this.userModal) {
      this.userModal.isEditMode = true;
      // Optionally pass user data to the modal for editing
      this.userModal.openModal();
    } else {
      console.error('UserModalComponent is not initialized');
    }
  }

}
