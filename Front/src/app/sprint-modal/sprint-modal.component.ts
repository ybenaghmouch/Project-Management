import { Component, ElementRef, ViewChild, Input, EventEmitter, Output, OnInit, AfterViewInit, inject } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { SprintModalService } from './service/sprint-modal.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';
import { NgbModalModule, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';
import { map, filter, takeWhile } from 'rxjs/operators';
import { TagInputModule } from 'ngx-chips';
import { NgbAlertModule, NgbDatepickerModule, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { NgbCalendar, NgbDate, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { JsonPipe } from '@angular/common';
import { DatePipe } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';
import { UtilsService } from '../utils';

declare var bootstrap: any;

@Component({
  selector: 'app-sprint-modal',
  standalone: true,
  imports: [NgIf, NgFor, ReactiveFormsModule, FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModalModule,
    NgbPaginationModule,
    NgSelectModule, TagInputModule, NgbDatepickerModule, NgbAlertModule, JsonPipe],
  providers: [SprintModalService, DatePipe, UtilsService, UtilsService],
  templateUrl: './sprint-modal.component.html',
  styleUrl: './sprint-modal.component.css'
})
export class SprintModalComponent implements OnInit {
  form: FormGroup;
  userstories: any[] = [];
  @Output() sprintAdded = new EventEmitter<any>();
  @ViewChild('sprintModal', { static: false }) sprintModal!: ElementRef;
  @Input() isEditMode: boolean = false;
  @Input() projectname: string = '';
  @Input() sprint: any;

  constructor(private cdr: ChangeDetectorRef, private utilsService: UtilsService, private formatter: NgbDateParserFormatter, private formBuilder: FormBuilder, private sprintModalService: SprintModalService, private userService: SprintModalService, private http: HttpClient, private datePipe: DatePipe) {
    this.form = this.formBuilder.group({
      titre: ['', Validators.required],
      description: ['', Validators.required],
      date_debut: ['', Validators.required],
      date_fin: ['', Validators.required],
      status: ['', Validators.required],
      userStories: [[], Validators.required],
    });

  }

  ngOnInit(): void {
    this.loadUserStories();
    this.subscribeToDateChanges();
    if (this.sprint) {
      this.form.patchValue(this.sprint);
    }
  }
  loadUserStories() {
    this.sprintModalService.getUserStories().subscribe(userstories => {
        this.userstories = userstories;
        console.log('Loaded user stories:', this.userstories);
    }, error => {
        console.error('Error loading user stories:', error);
    });
}

  subscribeToDateChanges() {
    const startDateControl = this.form.get('date_debut');
    const endDateControl = this.form.get('date_fin');

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
    const startDate = this.form.get('date_debut')?.value;
    const endDate = this.form.get('date_fin')?.value;
    if (startDate && endDate) {
      this.utilsService.calculateDuration(startDate, endDate).subscribe(
        duration => {
          this.form.patchValue({ duration: duration });
        },
        error => {
          console.error('Error calculating duration:', error);
        }
      );
    }
  }
  calculateDuration(startDate: string, endDate: string): number {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const duration = (end.getTime() - start.getTime()) / (1000 * 3600 * 24); // Convert to days
    return Math.max(0, duration); // Ensure non-negative duration
  }


  private formatDate(dateStr: string): string {
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
  }

  openModal() {
    console.log('Opening modal with sprint:', this.sprint);

    if (this.sprint) {
        const DateDebut = this.formatDate(this.sprint.date_debut);
        const dateFin = this.formatDate(this.sprint.date_fin);

        let userStoriesIds: number[] = [];

        // Check if userStories is defined and is an array before mapping
        if (Array.isArray(this.sprint.userStories)) {
            userStoriesIds = this.sprint.userStories.map((us: { id: number }) => us.id);
        } else {
            console.error('userStories is not defined or not an array:', this.sprint.userStories);
        }

        console.log('us id : ', userStoriesIds);

        // Patch form values
        this.form.patchValue({
            titre: this.sprint.titre,
            description: this.sprint.description,
            date_debut: DateDebut,
            date_fin: dateFin,
            status: this.sprint.status,
            userStories: userStoriesIds,
        });
    } else {
        this.form.reset();
    }

    const modalElement = this.sprintModal.nativeElement;
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
}


  closeModal() {
    const modalElement = this.sprintModal.nativeElement;
    const modalInstance = bootstrap.Modal.getInstance(modalElement);
    this.form.reset();
    modalInstance.hide();
  }

  submitForm() {
    if (this.form.valid) {
      const formData = this.form.value;
      const DateDebut = this.formatDate(formData.date_debut);
      const DateFin = this.formatDate(formData.date_fin);

      console.log("Formatted DateDebut:", DateDebut);
      console.log("Formatted DateFin:", DateFin);

      if (new Date(DateDebut) >= new Date(DateFin)) {
        alert("La date de début doit être antérieure à la date de fin.");
        return;
      }
      //const userStoriesIds = formData.userStories.map((id: number) => ({ id: id }));
      const sprintData = {
        titre: formData.titre,
        description: formData.description,
        date_debut: DateDebut,
        date_fin: DateFin,
        status: formData.status,
        userStories: formData.userStories.map((code: string) => ({code})),
      };
      console.log("Sending Sprint Data:", sprintData);
      console.log("Project Name : ", this.projectname);
      console.log("userStoriesIds ", sprintData.userStories);

      if (this.isEditMode) {
          this.sprintModalService.putSprint(sprintData, sprintData.titre).subscribe((response: any) => {
              this.sprintAdded.emit(response);
              this.closeModal();
          });
      } else {
          this.sprintModalService.postSprint(sprintData, this.projectname).subscribe((response: any) => {
              this.sprintAdded.emit(response);
              this.closeModal();
          });
      }
      this.closeModal();
      this.form.reset();
    }
  }

  // submitForm(){
  //   if(this.sprint){
  //     const formData = this.form.value;
  //     this.sprintModalService.putSprint(formData, formData.titre).subscribe(
  //       (data: any) =>{
  //         this.sprint = data;
  //       },
  //       error =>{
  //         console.error('Error fetching sprints', error);

  //       }
  //     );
  //     this.sprintAdded.emit({ ...this.form.value, titre: this.sprint.titre});
  //   }else{
  //     if(this.form.valid){
  //       const formData = this.form.value;
  //       this.sprintModalService.postSprint(formData, this.projectname).subscribe(
  //         (data: any) =>{
  //           this.sprint = data;
  //         },
  //         error => {
  //           console.error('Error fetching sprints', error);
  //         }
  //       );
  //       this.sprintAdded.emit(formData);
  //     }
  //     this.closeModal();
  //     this.form.reset();
  //   }
  // }


}
