import { Component, ElementRef, ViewChild, Input, EventEmitter, Output, OnInit } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TaskModalService } from './service/task-modal.service';
import { TagInputModule } from 'ngx-chips';
import { NgbModalModule, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';
declare var bootstrap: any;

@Component({
  selector: 'app-task-modal',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, ReactiveFormsModule, TagInputModule, NgbModalModule, NgbPaginationModule, NgSelectModule],
  providers: [TaskModalService],
  templateUrl: './task-modal.component.html',
  styleUrls: ['./task-modal.component.css']
})
export class TaskModalComponent implements OnInit {
  form: FormGroup;
  users: any[] = [];
  priorityRange: number[] = Array.from({ length: 11 }, (_, i) => i);
  @Output() userAdded = new EventEmitter<any>();

  @ViewChild('userModal') userModal!: ElementRef;
  @Input() isEditMode: boolean = false;
  @Input() isListMode: boolean = false;
  @Input() projectname: string = '';
  @Input() user: any;

  constructor(private formBuilder: FormBuilder, private userModalService: TaskModalService) {
    this.form = this.formBuilder.group({
      id: [''],
      titre: ['', Validators.required],
      description: ['', Validators.required],
      statut: ['pending', Validators.required],
      responsable: [null],
      priority: [0, Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userModalService.getUsers().subscribe(users => {
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
  }

  submitForm() {
    if (this.form.valid) {
      const formData = { ...this.form.value };
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
