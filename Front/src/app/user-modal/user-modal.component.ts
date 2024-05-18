import { Component, ElementRef, ViewChild, Input } from '@angular/core';
declare var bootstrap: any;


@Component({
  selector: 'app-user-modal',
  standalone: true,
  imports: [],
  templateUrl: './user-modal.component.html',
  styleUrl: './user-modal.component.css'
})
export class UserModalComponent {

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
