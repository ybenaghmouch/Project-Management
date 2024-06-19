import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe, CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HolidayService } from './service/holidays.service';

@Component({
  selector: 'app-holidays',
  standalone: true,
  templateUrl: './holidays.component.html',
  styleUrls: ['./holidays.component.css'],
  imports: [CommonModule, FormsModule, HttpClientModule, ReactiveFormsModule, NgSelectModule, NgbModule],
  providers: [HolidayService, DatePipe]
})
export class HolidaysComponent implements OnInit {
  holidays: any[] = [];
  holidayForm: FormGroup;
  isEditMode = false;
  selectedHoliday: any | null = null;

  constructor(
    private fb: FormBuilder,
    private modalService: NgbModal,
    private holidayService: HolidayService,
    private datePipe: DatePipe
  ) {
    this.holidayForm = this.fb.group({
      name: ['', Validators.required],
      duration: [{ value: 0, disabled: true }, Validators.required],
      annual: [true, Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadHolidays();
    this.subscribeToDateChanges();
  }

  loadHolidays() {
    this.holidayService.getAllHolidays().subscribe(holidays => this.holidays = holidays);
  }

  openCreateHolidayModal(content: any) {
    this.isEditMode = false;
    this.selectedHoliday = null;
    this.holidayForm.reset({ annual: true, duration: 0 });
    this.modalService.open(content);
  }

  openEditHolidayModal(holiday: any, content: any) {
    this.isEditMode = true;
    this.selectedHoliday = holiday;
    this.holidayForm.patchValue({
      name: holiday.name,
      duration: holiday.duration,
      annual: holiday.annual,
      startDate: this.datePipe.transform(holiday.startDate, 'yyyy-MM-dd'),
      endDate: this.datePipe.transform(holiday.endDate, 'yyyy-MM-dd')
    });
    this.modalService.open(content);
  }

  saveHoliday(modal: NgbModalRef) {
    const holidayData = this.holidayForm.getRawValue();

    if (this.isEditMode && this.selectedHoliday) {
      this.holidayService.updateHoliday(this.selectedHoliday.id, holidayData).subscribe(() => {
        this.loadHolidays();
        modal.dismiss();
      });
    } else {
      this.holidayService.createHoliday(holidayData).subscribe(() => {
        this.loadHolidays();
        modal.dismiss();
      });
    }
  }

  deleteHoliday(holiday: any) {

    this.holidayService.deleteHoliday(holiday.id).subscribe(() => this.loadHolidays());
    const index = this.holidays.indexOf(holiday, 0);
    if (index > -1) {
      this.holidays.splice(index, 1);
   }
  }

  closeModal(modal: NgbModalRef) {
    modal.dismiss();
  }

  subscribeToDateChanges() {
    const startDateControl = this.holidayForm.get('startDate');
    const endDateControl = this.holidayForm.get('endDate');

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
    const startDate = this.holidayForm.get('startDate')?.value;
    const endDate = this.holidayForm.get('endDate')?.value;
    if (startDate && endDate) {
      const duration = this.calculateDuration(startDate, endDate);
      this.holidayForm.patchValue({ duration: duration });
    }
  }

  calculateDuration(startDate: string, endDate: string): number {
    const start = new Date(startDate);
    const end = new Date(endDate);
    let duration = 0;
    let current = new Date(start);

    while (current <= end) {
      duration++;
      current.setDate(current.getDate() + 1);
    }

    return duration;
  }
}
