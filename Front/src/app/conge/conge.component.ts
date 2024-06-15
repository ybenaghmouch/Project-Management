import { Component, OnInit } from '@angular/core';
import { CongeService } from './service/conge.service';
import { NgSelectModule } from '@ng-select/ng-select';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgbModule, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ReactiveFormsModule, FormsModule, FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-conge',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, ReactiveFormsModule, NgSelectModule, NgbModule],
  providers: [CongeService],
  templateUrl: './conge.component.html',
  styleUrls: ['./conge.component.css']
})
export class CongeComponent implements OnInit {
  conges: any[] = [];
  congeForm: FormGroup;
  isEditMode = false;
  selectedConge: any | null = null;
  users: any[] = []; // Replace with actual user model

  constructor(private fb: FormBuilder, private modalService: NgbModal, private congeService: CongeService) {
    this.congeForm = this.fb.group({
      motif: [''],
      duration: [0],
      status: ['pending'],
      fromDate: [''],
      endDate: [''],
      demandeur: [null],
      backup: [null]
    });
  }

  ngOnInit(): void {
    this.loadConges();
    this.loadUsers();
    this.subscribeToDateChanges();
  }

  loadConges() {
    this.congeService.getAllConges().subscribe(conges => this.conges = conges);
  }

  loadUsers() {
    // Replace with actual service call to get users
    // this.userService.getAllUsers().subscribe(users => this.users = users);
    this.users = [
      { id: 1, username: 'User 1' },
      { id: 2, username: 'User 2' },
      { id: 3, username: 'User 3' }
    ];
  }

  openCreateCongeModal(content: any) {
    this.isEditMode = false;
    this.selectedConge = null;
    this.congeForm.reset({ status: 'pending', duration: 0 });
    this.modalService.open(content);
  }

  openEditCongeModal(conge: any, content: any) {
    this.isEditMode = true;
    this.selectedConge = conge;
    this.congeForm.patchValue(conge);
    this.modalService.open(content);
  }

  saveConge(modal: NgbModalRef) {
    const congeData = this.congeForm.value;
    if (this.isEditMode && this.selectedConge) {
      this.congeService.updateConge(this.selectedConge.id, congeData).subscribe(() => {
        this.loadConges();
        modal.dismiss();
      });
    } else {
      this.congeService.createConge(congeData).subscribe(() => {
        this.loadConges();
        modal.dismiss();
      });
    }
  }

  updateCongeStatus(conge: any, status: string) {
    const updatedConge = { ...conge, status };
    this.congeService.updateConge(conge.id, updatedConge).subscribe(() => this.loadConges());
  }

  closeModal(modal: NgbModalRef) {
    modal.dismiss();
  }

  subscribeToDateChanges() {
    const startDateControl = this.congeForm.get('fromDate');
    const endDateControl = this.congeForm.get('endDate');

    if (startDateControl && endDateControl) {
      startDateControl.valueChanges.subscribe(() => {
        this.updateDuration();
      });
      endDateControl.valueChanges.subscribe(() => {
        this.updateDuration();
      });
    }
  }

  updateDuration() {
    const startDate = this.congeForm.get('fromDate')?.value;
    const endDate = this.congeForm.get('endDate')?.value;
    if (startDate && endDate) {
      const duration = this.calculateDuration(startDate, endDate);
      this.congeForm.patchValue({ duration: duration });
    }
  }

  calculateDuration(startDate: string, endDate: string): number {
    const start = new Date(startDate);
    const end = new Date(endDate);
    let duration = 0;
    let current = new Date(start);

    while (current <= end) {
      const day = current.getDay();
      if (day !== 0 && day !== 6) { // Exclude Sunday (0) and Saturday (6)
        duration++;
      }
      current.setDate(current.getDate() + 1);
    }

    return duration; // Ensure non-negative duration
  }
}
