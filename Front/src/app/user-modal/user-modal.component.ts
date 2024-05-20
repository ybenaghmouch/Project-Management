import { Component, ElementRef, ViewChild, Input, EventEmitter, Output  } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserModalService } from './service/user-modal.service';
import { HttpClientModule } from '@angular/common/http';
declare var bootstrap: any;

@Component({
  selector: 'app-user-modal',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, ReactiveFormsModule],
  providers: [UserModalService],
  templateUrl: './user-modal.component.html',
  styleUrls: ['./user-modal.component.css']
})
export class UserModalComponent  {
  form: FormGroup;
  
  @Output() userAdded = new EventEmitter<any>();

  @ViewChild('userModal') userModal!: ElementRef;
  @Input() isEditMode: boolean =false;
  @Input() user: any;

  constructor(private formBuilder: FormBuilder,private userModalService:UserModalService) {
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

    if (this.user){
      const formData = this.form.value;
      this.userModalService.putUsers(formData,formData.username,formData.role.toLowerCase()).subscribe(
        (data: any) => {
          this.user = data;
        },
        error => {
          console.error('Error fetching users', error);
        }
      );
      this.userAdded.emit({ ...this.form.value, username: this.user.username });
      
    }else{
    if (this.form.valid) {
      
      const formData = this.form.value;
      this.userModalService.postUsers(formData).subscribe(
        (data: any) => {
          this.user = data;
        },
        error => {
          console.error('Error fetching users', error);
        }
      );
      
      
      
      
      this.userAdded.emit(formData);
      
    }
    
  }
  this.form.reset();
  this.closeModal();
  }
}
