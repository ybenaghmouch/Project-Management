import { Component, ElementRef, ViewChild, Input, EventEmitter, Output, OnInit } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserModalService } from './service/user-modal.service';
import { HttpClientModule } from '@angular/common/http';
import { NgSelectModule } from '@ng-select/ng-select';
import { RoleListService } from '../role-list/service/role-list.service';

declare var bootstrap: any;

@Component({
  selector: 'app-user-modal',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, ReactiveFormsModule, NgSelectModule],
  providers: [UserModalService, RoleListService],
  templateUrl: './user-modal.component.html',
  styleUrls: ['./user-modal.component.css']
})
export class UserModalComponent implements OnInit {
  form: FormGroup;
  roles: any[] = [];

  @Output() userAdded = new EventEmitter<any>();

  @ViewChild('userModal') userModal!: ElementRef;
  @Input() isEditMode: boolean = false;
  @Input() isListMode: boolean = false;
  @Input() user: any;

  constructor(private formBuilder: FormBuilder, private userModalService: UserModalService, private roleListService: RoleListService) {
    this.form = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      password: [''],
      email: ['', [Validators.required, Validators.email]],
      speciality: ['', Validators.required],
      civility: ['Mr.', Validators.required],
      status: [true, Validators.required],
      authorities: [[], Validators.required]
    });
  }

  ngOnInit(): void {
    this.getRoles();
  }

  openModal() {
    if (this.user) {
      this.form.patchValue({
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        username: this.user.username,
        password: '',
        email: this.user.email,
        speciality: this.user.speciality,
        civility: this.user.civility,
        status: this.user.status,
        authorities: this.user.authorities.map((role: any) => role.id)
      });
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

  getRoles(): void {
    this.roleListService.getRoles().subscribe(
      (roles) => {
        this.roles = roles;
      },
      (error) => {
        console.error('Error fetching roles', error);
      }
    );
  }

  submitForm() {
    if (this.form) {
      const formData = this.form.value;
      const userData: any = {
        firstName: formData.firstName,
        lastName: formData.lastName,
        username: formData.username,
        email: formData.email,
        speciality: formData.speciality,
        civility: formData.civility,
        status: formData.status,
        authorities: formData.authorities.map((id: number) => ({ id }))
      };

      // Conditionally add the password field if it is provided
      if (formData.password) {
        userData.password = formData.password;
      }

      if (this.isEditMode) {
        this.userModalService.putUsers(userData, formData.username).subscribe(
          (data: any) => {
            this.user = data;
            const formValueWithAuthorities = {
              ...this.form.value,
              firstName: this.user.firstName,
              lastName: this.user.lastName,
              username: this.user.username,
              password: '',
              email: this.user.email,
              speciality: this.user.speciality,
              civility: this.user.civility,
              status: this.user.status,
              authorities: this.user.authorities // Override authorities from form value with authorities from data
            };
            this.userAdded.emit(formValueWithAuthorities);
          },
          error => {
            console.error('Error updating user', error);
          }
        );
      } else {
        if (this.form.valid) {
          this.userModalService.postUsers(userData).subscribe(
            (data: any) => {
              this.user = data;
              this.userAdded.emit(data);
            },
            error => {
              console.error('Error adding user', error);
            }
          );
        }
      }

      this.form.reset();
      this.closeModal();
    }
  }
}
