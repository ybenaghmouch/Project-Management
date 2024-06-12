import { Component, ElementRef, ViewChild, Input, EventEmitter, Output, OnInit } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RoleModalService } from './service/role-modal.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';
import { NgbModalModule, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';
import { map } from 'rxjs/operators';
import { TagInputModule } from 'ngx-chips';

declare var bootstrap: any;

@Component({
  selector: 'app-role-modal',
  standalone: true,
  imports: [
    NgIf, NgFor, ReactiveFormsModule,
    FormsModule, HttpClientModule,
    NgbModalModule, NgbPaginationModule,
    NgSelectModule, TagInputModule
  ],
  providers: [RoleModalService],
  templateUrl: './role-modal.component.html',
  styleUrls: ['./role-modal.component.css']
})
export class RoleModalComponent implements OnInit {
  form: FormGroup;
  users: any[] = [];
  items = ['Javascript', 'Typescript'];
  @Output() roleAdded = new EventEmitter<any>();
  @ViewChild('roleModal', { static: false }) roleModal!: ElementRef;
  @Input() isEditMode: boolean = false;
  @Input() isListMode: boolean = false;
  @Input() role: any;

  modules = [
    { key: 'backlog', name: 'Backlog' },
    { key: 'equipe', name: 'Equipe' },
   /* { key: 'manager', name: 'Manager' },*/
    { key: 'project', name: 'Project' },
    { key: 'tache', name: 'Tache' },
    { key: 'user', name: 'User' },
    { key: 'userstory', name: 'Userstory' }
  ];

  permissionsMap: { [key: string]: { read: number; write: number; edit: number; delete: number } } = {
    backlog: { read: 1, write: 2, edit: 3, delete: 4 },
    equipe: { read: 5, write: 6, edit: 7, delete: 8 },
   /* manager: { read: 9, write: 10, edit: 11, delete: 12 },*/
    project: { read: 13, write: 14, edit: 15, delete: 16 },
    tache: { read: 17, write: 18, edit: 19, delete: 20 },
    user: { read: 21, write: 22, edit: 23, delete: 24 },
    userstory: { read: 25, write: 26, edit: 27, delete: 28 }
  };

  constructor(
    private formBuilder: FormBuilder,
    private roleModalService: RoleModalService,
    private http: HttpClient
  ) {
    this.form = this.formBuilder.group({});
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      authority: [''],
      ...this.modules.reduce((acc, module) => ({
        ...acc,
        ['read_' + module.key]: [false],
        ['write_' + module.key]: [false],
        ['edit_' + module.key]: [false],
        ['delete_' + module.key]: [false]
      }), {})
    });
  }

  requestAutocompleteItems = (text: string): Observable<any> => {
    const url = `https://api.github.com/search/repositories?q=${text}&per_page=2`;
    return this.http.get<any>(url).pipe(
      map(data => data.items.map((item: any) => item.full_name))
    );
  };

  openModal() {
    if (this.role) {

      const authorities = this.role.authorities.map((auth: any) => auth.id);
      this.form.patchValue({
        authority: this.role.authority,
        ...this.modules.reduce((acc, module) => ({
          ...acc,
          ['read_' + module.key]: authorities.includes(this.permissionsMap[module.key].read),
          ['write_' + module.key]: authorities.includes(this.permissionsMap[module.key].write),
          ['edit_' + module.key]: authorities.includes(this.permissionsMap[module.key].edit),
          ['delete_' + module.key]: authorities.includes(this.permissionsMap[module.key].delete)
        }), {})
      });
    } else {
      this.form.reset();
    }
    const modalElement = this.roleModal.nativeElement;
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
  }

  closeModal() {
    const modalElement = this.roleModal.nativeElement;
    const modalInstance = bootstrap.Modal.getInstance(modalElement);
    this.form.reset();
    modalInstance.hide();
  }

  submitForm() {
    if (this.form.valid) {
      const formData = this.form.value;
      const selectedPermissions: number[] = [];

      this.modules.forEach(module => {
        if (formData['read_' + module.key]) {
          selectedPermissions.push(this.permissionsMap[module.key].read);
        }
        if (formData['write_' + module.key]) {
          selectedPermissions.push(this.permissionsMap[module.key].write);
        }
        if (formData['edit_' + module.key]) {
          selectedPermissions.push(this.permissionsMap[module.key].edit);
        }
        if (formData['delete_' + module.key]) {
          selectedPermissions.push(this.permissionsMap[module.key].delete);
        }
      });

      const roleData = {
        authority: formData.authority,
        authorities: selectedPermissions.map(id => ({ id }))
      };

      if (this.isEditMode) {
        this.roleModalService.putRole(roleData, roleData.authority).subscribe((response: any) => {
          this.roleAdded.emit(response);
          this.closeModal();
        });
      } else
      if(this.isListMode){
        this.roleModalService.putRole(roleData, roleData.authority).subscribe((response: any) => {
          this.roleAdded.emit(response);
          this.closeModal();
        });
      }else {
        this.roleModalService.postRole(roleData).subscribe((response: any) => {
          this.roleAdded.emit(response);
          this.closeModal();
        });
      }
    }
  }
}
