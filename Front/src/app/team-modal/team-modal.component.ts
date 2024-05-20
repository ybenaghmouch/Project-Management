import { Component, ElementRef, ViewChild, Input, EventEmitter, Output  } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TeamModalService } from './service/team-modal.service';
declare var bootstrap: any;

@Component({
  selector: 'app-team-modal',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, ReactiveFormsModule],
  providers: [TeamModalService],
  templateUrl: './team-modal.component.html',
  styleUrl: './team-modal.component.css'
})
export class TeamModalComponent {
  form: FormGroup;
  
  @Output() teamAdded = new EventEmitter<any>();

  @ViewChild('teamModal') teamModal!: ElementRef;
  @Input() isEditMode: boolean =false;
  @Input() team: any;

  constructor(private formBuilder: FormBuilder,private teamModalService:TeamModalService) {
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
    if (this.team) {
      this.form.patchValue(this.team);
    }
    
    const modalElement = this.teamModal.nativeElement;
    console.log("blablba");
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
  }

  closeModal() {
    const modalElement = this.teamModal.nativeElement;
    const modalInstance = bootstrap.Modal.getInstance(modalElement);
    this.form.reset();
    modalInstance.hide();
  }

  submitForm() {

    if (this.team){
      const formData = this.form.value;
      this.teamModalService.putTeams(formData,formData.username,formData.role.toLowerCase()).subscribe(
        (data: any) => {
          this.team = data;
        },
        error => {
          console.error('Error fetching users', error);
        }
      );
      this.teamAdded.emit({ ...this.form.value, username: this.team.username });
      
    }else{
    if (this.form.valid) {
      
      const formData = this.form.value;
      this.teamModalService.postTeams(formData).subscribe(
        (data: any) => {
          this.team = data;
        },
        error => {
          console.error('Error fetching users', error);
        }
      );
      
      
      
      
      this.teamAdded.emit(formData);
      
    }
    
  }
  this.form.reset();
  this.closeModal();
  }

}
