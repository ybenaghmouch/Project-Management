import { Component, ElementRef, ViewChild, Input, EventEmitter, Output, OnInit, AfterViewInit, inject } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { ProjectModalService } from './service/project-modal.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';
import { NgbModalModule, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';
import { map, filter } from 'rxjs/operators';
import { TagInputModule } from 'ngx-chips';
import { NgbAlertModule, NgbDatepickerModule, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { NgbCalendar, NgbDate, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { JsonPipe } from '@angular/common';
import { DatePipe } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';
import { UtilsService } from '../utils';



declare var bootstrap: any;


@Component({
  selector: 'app-project-modal',
  standalone: true,
  imports: [NgIf, NgFor, ReactiveFormsModule, FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModalModule,
    NgbPaginationModule,
    NgSelectModule, TagInputModule, NgbDatepickerModule, NgbAlertModule, JsonPipe],
  providers: [ProjectModalService, DatePipe,UtilsService],
  templateUrl: './project-modal.component.html',
  styleUrl: './project-modal.component.css'
})
export class ProjectModalComponent implements OnInit {

  form: FormGroup;
  users: any[] = [];
  equipes: any[] = [];
  @Output() projectAdded = new EventEmitter<any>();
  @ViewChild('projectModal', { static: false }) projectModal!: ElementRef;
  @Input() isEditMode: boolean = false;
  @Input() isListMode: boolean = false;
  @Input() project: any;


  constructor(private cdr: ChangeDetectorRef,private utilsService: UtilsService, private formatter: NgbDateParserFormatter, private formBuilder: FormBuilder, private projectModalService: ProjectModalService, private userService: ProjectModalService, private http: HttpClient, private datePipe: DatePipe) {
    this.form = this.formBuilder.group({
      nom: ['', Validators.required],
      description: ['', Validators.required],
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required],
      duration: ['', Validators.required],
      status: ['', Validators.required],
      manager: [null, Validators.required],
      equipe: [null, Validators.required],
      //backlogs: [[], Validators.required],
    });

  }

  ngOnInit(): void {
    this.loadUsers();
    this.loadTeams();
    this.subscribeToDateChanges();
    if (this.project) {
      this.form.patchValue(this.project);
    }
  }

  subscribeToDateChanges() {
    const startDateControl = this.form.get('dateDebut');
    const endDateControl = this.form.get('dateFin');

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
    const startDate = this.form.get('fromDate')?.value;
    const endDate = this.form.get('endDate')?.value;
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


  loadUsers() {
    this.userService.getUsers().subscribe(users => {
      this.users = users;
    });
  }

  loadTeams(){
    this.userService.getTeams().subscribe(equipes =>{
      this.equipes = equipes;
    });
  }


  private formatDate(dateStr: string): string {
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
  }

  openModal() {
    if (this.project) {
       const DateDebut = this.formatDate(this.project.dateDebut);
       const dateFin = this.formatDate(this.project.dateFin);
      this.form.patchValue({
        nom: this.project.nom,
        description: this.project.description,
        dateDebut: DateDebut,
        dateFin: dateFin,
        duration: this.project.duree,
        status: this.project.status,
        manager: this.project.manager.id,
        equipe: this.project.equipe.id,
      });

    } else {
      this.form.reset();
    }

    const modalElement = this.projectModal.nativeElement;
    const modalInstance = new bootstrap.Modal(modalElement);
    modalInstance.show();
  }

  closeModal() {
    const modalElement = this.projectModal.nativeElement;
    const modalInstance = bootstrap.Modal.getInstance(modalElement);
    this.form.reset();
    modalInstance.hide();
    this.isListMode = false;
    this.form.enable();
  }


  submitForm() {
    if (this.form.valid) {
        const formData = this.form.value;
        const DateDebut = this.formatDate(formData.dateDebut);
        const DateFin = this.formatDate(formData.dateFin);

        console.log("Formatted DateDebut:", DateDebut);
        console.log("Formatted DateFin:", DateFin);

        if (new Date(DateDebut) >= new Date(DateFin)) {
            alert("La date de début doit être antérieure à la date de fin.");
            return;
        }

        const projectData = {
            nom: formData.nom,
            description: formData.description,
            dateDebut: DateDebut,
            dateFin: DateFin,
            duree: formData.duration,
            status: formData.status,
            manager: { id: formData.manager },
            equipe: { id : formData.equipe},
        };
        console.log("Sending Project Data:", projectData);

        if (this.isEditMode) {
            this.projectModalService.putProject(projectData, projectData.nom).subscribe((response: any) => {
                this.projectAdded.emit(response);
                this.closeModal();
            });
        } else {
            this.projectModalService.postProject(projectData).subscribe((response: any) => {
                this.projectAdded.emit(response);
                this.closeModal();
            });
        }
    }
}



}
