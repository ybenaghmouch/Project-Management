import { Component, ElementRef, ViewChild, Input } from '@angular/core';
import { NgIf, NgFor  } from '@angular/common';
import { FormsModule  } from '@angular/forms';
declare var bootstrap: any;


@Component({
  selector: 'app-user-modal',
  standalone: true,
  imports: [NgIf, NgFor,FormsModule],
  templateUrl: './user-modal.component.html',
  styleUrl: './user-modal.component.css'
})
export class UserModalComponent {
  statut: boolean =false;  // Property to bind to the switch
  @ViewChild('userModal') userModal!: ElementRef;
  @Input() isEditMode: boolean = false;

  constructor() {}

  openModal() {
    const modalElement = this.userModal.nativeElement;
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
  }

  closeModal() {
    const modalElement = this.userModal.nativeElement;
    const modalInstance = bootstrap.Modal.getInstance(modalElement);
    modalInstance.hide();
  }
  
}
