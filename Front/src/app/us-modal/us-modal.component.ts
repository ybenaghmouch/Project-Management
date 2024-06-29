import { Component, ElementRef, ViewChild, Input, EventEmitter, Output, OnInit  } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsModalService } from './service/us-modal.service';
import { HttpClientModule } from '@angular/common/http';
import { NgSelectModule } from '@ng-select/ng-select';
declare var bootstrap: any;

@Component({
  selector: 'app-us-modal',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, ReactiveFormsModule, NgSelectModule],
  providers: [UsModalService],
  templateUrl: './us-modal.component.html',
  styleUrls: ['./us-modal.component.css']
})
export class UsModalComponent implements OnInit{
  form: FormGroup;
  users: any[] = [];
  priorityRange: number[] = Array.from({ length: 11 }, (_, i) => i);
  @Output() userAdded = new EventEmitter<any>();

  @ViewChild('userModal') userModal!: ElementRef;
  @Input() isEditMode: boolean = false;
  @Input() isListMode: boolean = false;
  @Input() projectname: string = '';
  @Input() user: any;

  constructor(private formBuilder: FormBuilder, private userModalService: UsModalService) {
    this.form = this.formBuilder.group({
      titre: ['', Validators.required],
      description: ['', Validators.required],
      statut: ['pending', Validators.required],
      priority: [0, Validators.required],
      responsable: ['', Validators.required],
    });
  }


  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userModalService.getUsers().subscribe((users: any) => {
      this.users = users;
    });
  }


  openModal() {
    if (this.user) {
      const userToPatch = { ...this.user, responsable: this.user.responsable?.id || null };
      this.form.patchValue(userToPatch);
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
    if (this.form.valid) {
      const formData = this.form.value;
      if (formData.responsable == null) {
        delete formData.responsable;
      } else {
        formData.responsable = { id: formData.responsable };
      }
      if (this.user) {
        this.userModalService.putUsers(formData, this.user.code).subscribe(
          (data: any) => {
            this.userAdded.emit(data);
          },
          error => {
            console.error('Error updating user', error);
          }
        );
      } else {
        this.userModalService.postUsers(formData, this.projectname).subscribe(
          (data: any) => {
            this.userAdded.emit(data);
          },
          error => {
            console.error('Error creating user', error);
          }
        );
      }
      this.form.reset();
      this.closeModal();
    }
  }
}
