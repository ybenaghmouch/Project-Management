import { Component, ElementRef, ViewChild, Input, EventEmitter, Output  } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BacklogModalService } from './service/backlog-modal.service';
import { HttpClientModule } from '@angular/common/http';
declare var bootstrap: any;

@Component({
  selector: 'app-backlog-modal',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, ReactiveFormsModule],
  providers: [BacklogModalService],
  templateUrl: './backlog-modal.component.html',
  styleUrl: './backlog-modal.component.css'
})
export class BacklogModalComponent {
  form: FormGroup;

  @Output() userAdded = new EventEmitter<any>();

  @ViewChild('userModal') userModal!: ElementRef;
  @Input() isEditMode: boolean =false;
  @Input() isListMode: boolean =false;
  @Input() projectname: string ='';
  @Input() user: any;

  constructor(private formBuilder: FormBuilder,private userModalService:BacklogModalService) {
    this.form = this.formBuilder.group({

      titre: [ '', Validators.required ],
      description: ['', Validators.required],
      status: ['', Validators.required]
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
    this.isListMode = false;
    this.form.enable();
  }

  submitForm() {

    if (this.user){
      const formData = this.form.value;
      this.userModalService.putUsers(formData,formData.titre).subscribe(
        (data: any) => {
          this.user = data;
        },
        error => {
          console.error('Error fetching users', error);
        }
      );
      this.userAdded.emit({ ...this.form.value, titre: this.user.titre });

    }else{
    if (this.form.valid) {

      const formData = this.form.value;
      this.userModalService.postUsers(formData,this.projectname).subscribe(
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
