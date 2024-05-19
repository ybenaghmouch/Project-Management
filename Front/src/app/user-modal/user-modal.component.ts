import { Component, ElementRef, ViewChild, Input, EventEmitter, Output,OnChanges  } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
declare var bootstrap: any;

@Component({
  selector: 'app-user-modal',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, ReactiveFormsModule],
  templateUrl: './user-modal.component.html',
  styleUrls: ['./user-modal.component.css']
})
export class UserModalComponent  {
  form: FormGroup;

  @Output() userAdded = new EventEmitter<any>();

  @ViewChild('userModal') userModal!: ElementRef;
  @Input() isEditMode: boolean =false;
  @Input() user: any;

  constructor(private formBuilder: FormBuilder) {
    this.form = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', Validators.required],
      speciality: ['', Validators.required],
      civility: ['', Validators.required],
      status: [true],
      role: ['', Validators.required]
    });
  }


  openModal() {
    if (this.user) {
      this.form.patchValue(this.user);
    }
    const modalElement = this.userModal.nativeElement;
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
  }

  closeModal() {
    const modalElement = this.userModal.nativeElement;
    const modalInstance = bootstrap.Modal.getInstance(modalElement);
    this.form.reset();
    modalInstance.hide();
  }

  submitForm() {
    if (this.form.valid) {
      const formData = this.form.value;
      this.userAdded.emit(formData);
      
      console.log(formData);
      this.form.reset();
      this.closeModal();
    }
  }
}
