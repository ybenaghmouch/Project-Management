import { Component, ElementRef, ViewChild, Input, EventEmitter, Output, OnInit } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TeamModalService } from './service/team-modal.service';
import { UserListService } from '../user-list/service/user-list.service';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { NgbModalModule, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';

declare var bootstrap: any;

@Component({
  selector: 'app-team-modal',
  standalone: true,
  imports: [NgIf, NgFor, ReactiveFormsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModalModule,
    NgbPaginationModule,
    NgSelectModule],
  providers: [TeamModalService],
  templateUrl: './team-modal.component.html',
  styleUrls: ['./team-modal.component.css']
})
export class TeamModalComponent implements OnInit {
  form: FormGroup;
  users: any[] = [];

  @Output() teamAdded = new EventEmitter<any>();
  @ViewChild('teamModal') teamModal!: ElementRef;
  @Input() isEditMode: boolean = false;
  @Input() team: any;

  constructor(private formBuilder: FormBuilder, private teamModalService: TeamModalService, private userService: TeamModalService) {
    this.form = this.formBuilder.group({
      nom: ['', Validators.required],
      chefprojet: [null, Validators.required],
      manager: [null, Validators.required],
      collaborateurs: [[], Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getUsers().subscribe(users => {
      this.users = users;
    });

  }

  openModal() {
    if (this.team) {
      this.form.patchValue({
        nom: this.team.nom,
        chefprojet: this.team.chefprojet.id,
        manager: this.team.manager.id,
        collaborateurs: this.team.collaborateurs.map((collaborator: any) => collaborator.id)
      });
    } else {
      this.form.reset();
    }

    const modalElement = this.teamModal.nativeElement;
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
    if (this.form.valid) {
      const formData = this.form.value;
      const teamData = {
        nom: formData.nom,
        chefprojet: { id: formData.chefprojet },
        manager: { id: formData.manager },
        collaborateurs: formData.collaborateurs.map((id: number) => ({ id }))
      };

      if (this.isEditMode) {
        this.teamModalService.putTeam(teamData,teamData.nom).subscribe((response: any) => {
          this.teamAdded.emit(response);
          this.closeModal();
        });
      } else {
        this.teamModalService.postTeam(teamData).subscribe((response: any) => {
          this.teamAdded.emit(response);
          this.closeModal();
        });
      }
    }
  }
}
