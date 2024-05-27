import { Component , ElementRef, ViewChild, Input, EventEmitter, Output, OnInit ,AfterViewInit, inject} from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
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


declare var bootstrap: any;


@Component({
  selector: 'app-project-modal',
  standalone: true,
  imports: [NgIf, NgFor, ReactiveFormsModule,FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModalModule,
    NgbPaginationModule,
    NgSelectModule,TagInputModule, NgbDatepickerModule, NgbAlertModule, JsonPipe],
    providers: [ProjectModalService, DatePipe],
  templateUrl: './project-modal.component.html',
  styleUrl: './project-modal.component.css'
})
export class ProjectModalComponent implements OnInit{

  formattedDateDebut: string = '';

  calendar = inject(NgbCalendar);
	//formatter = inject(NgbDateParserFormatter);

	hoveredDate: NgbDate | null = null;
	fromDate: NgbDate | null = this.calendar.getToday();
	toDate: NgbDate | null = this.calendar.getNext(this.calendar.getToday(), 'd', 10);

	onDateSelection(date: NgbDate) {
		if (!this.fromDate && !this.toDate) {
			this.fromDate = date;
		} else if (this.fromDate && !this.toDate && date && date.after(this.fromDate)) {
			this.toDate = date;
		} else {
			this.toDate = null;
			this.fromDate = date;
		}
	}

	isHovered(date: NgbDate) {
		return (
			this.fromDate && !this.toDate && this.hoveredDate && date.after(this.fromDate) && date.before(this.hoveredDate)
		);
	}

	isInside(date: NgbDate) {
		return this.toDate && date.after(this.fromDate) && date.before(this.toDate);
	}

	isRange(date: NgbDate) {
		return (
			date.equals(this.fromDate) ||
			(this.toDate && date.equals(this.toDate)) ||
			this.isInside(date) ||
			this.isHovered(date)
		);
	}

	validateInput(currentValue: NgbDate | null, input: string): NgbDate | null {
		const parsed = this.formatter.parse(input);
		return parsed && this.calendar.isValid(NgbDate.from(parsed)) ? NgbDate.from(parsed) : currentValue;
	}



  form: FormGroup;
  users: any[] = [];
  items = ['Javascript', 'Typescript'];
  @Output() projectAdded = new EventEmitter<any>();
  @ViewChild('projectModal', { static: false }) projectModal!: ElementRef;
  @Input() isEditMode: boolean = false;
  @Input() project: any;


  constructor(private cdr: ChangeDetectorRef, private formatter: NgbDateParserFormatter,private formBuilder: FormBuilder, private projectModalService: ProjectModalService, private userService: ProjectModalService,private http: HttpClient, private datePipe: DatePipe) {
    this.form = this.formBuilder.group({
      nom: ['', Validators.required],
      description: ['', Validators.required],
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required],
      duration: ['', Validators.required],
      status: ['', Validators.required],
      manager: [[], Validators.required],
      backlogs: [[], Validators.required],
    });
    const dateDebutControl = this.form.get('dateDebut');
    if (dateDebutControl) {
      dateDebutControl.valueChanges.subscribe(date => {
        this.formattedDateDebut = this.datePipe.transform(date, 'yyyy-MM-dd') ?? '';
      });
    } else {
      // Optionally handle the case where the control doesn't exist
      console.error('dateDebut control is not found in the form!');
    }

  }



  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getUsers().subscribe(users => {
      this.users = users;
    });
  }




  private formatDate(dateStr: string): string {
    const date = new Date(dateStr);
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;}
  // transformDate(date: string | null): string | null {
  //   if (date === null) {
  //     return null;
  //   }
  //   return this.datePipe.transform(date, 'yyyy-MM-dd');
  // }


  openModal() {
    if (this.project) {
      const managerIds = this.project.manager ? [this.project.manager.id] : [];
      const DateDebut = this.formatDate(this.project.dateDebut);
      const dateFin = this.formatDate(this.project.dateFin);
      this.form.patchValue({
        nom: this.project.nom,
        description: this.project.description,
        dateDebut: DateDebut,
        dateFin: dateFin,
        duration: this.project.duree,
        status: this.project.status,
        manager: managerIds,
        backlogs: this.project.backlogs.map((backlog: any) => backlog.id)
      });
      console.log("DateDebut : ", DateDebut);

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
  }

  submitForm() {
    if (this.form.valid) {
      const formData = this.form.value;
      const projectData = {
        nom: formData.nom,
        description: formData.description,
        dateDebut: formData.dateDebut,
        dateFin: formData.dateFin,
        duration: formData.duration,
        status: formData.status,
        manager: { id: formData.manager },
        backlogs: formData.backlogs.map((id: number) => ({ id }))
      };

      if (this.isEditMode) {
        this.projectModalService.putProject(projectData,projectData.nom).subscribe((response: any) => {
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
