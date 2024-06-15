
import { Component, OnInit,TemplateRef  } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { CommonModule } from '@angular/common';
import { BoardService } from './service/board.service';
import {HttpClientModule, HttpClient } from '@angular/common/http';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BacklogModalComponent } from '../backlog-modal/backlog-modal.component';
import { ReactiveFormsModule,FormsModule,FormGroup,FormBuilder } from '@angular/forms';


import { RouterModule } from '@angular/router';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'app-board',
  standalone: true,
  imports: [CommonModule,NgbPaginationModule,BacklogModalComponent,FormsModule,HttpClientModule,ReactiveFormsModule,RouterModule, NgbDropdownModule,DragDropModule,],
  providers :[BoardService,HttpClient,HttpClientModule],
  templateUrl: './board.component.html',
  styleUrl: './board.component.css'
})
export class BoardComponent implements OnInit { 
  userStories: any[] = [];
  pending: any[] = [];
  started: any[] = [];
  done: any[] = [];
  selectedFeature: any;

  constructor(private userStoryService: BoardService, private modalService: NgbModal) { }

  ngOnInit(): void {
    // Fetch user stories from API
    this.userStoryService.getUserStories().subscribe(data => {
      this.userStories = data;
      this.splitUserStories();
    });
  }

  splitUserStories() {
    this.pending = this.userStories.filter(story => story.statut === 'pending');
    this.started = this.userStories.filter(story => story.statut === 'started');
    this.done = this.userStories.filter(story => story.statut === 'done');
  }

  drop(event: CdkDragDrop<any[]>, newStatus: string) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    }

    // Update the status of the feature
    const updatedFeature = event.container.data[event.currentIndex];
    updatedFeature.statut = newStatus;

    // Update the backend
    this.userStoryService.updateUserStory(updatedFeature).subscribe(() => {
      // Re-fetch the user stories to ensure the UI reflects the latest data
      this.userStoryService.getUserStories().subscribe(data => {
        this.userStories = data;
        this.splitUserStories();
      });
    });
  }

  openModal(feature: any, modalContent: TemplateRef<any>) {
    this.selectedFeature = feature;
    this.modalService.open(modalContent, { ariaLabelledBy: 'featureModalLabel' });
  }

  toggleFeatureStatus(feature: any) {
    feature.statut = feature.statut === 'done' ? 'pending' : 'done';
    this.userStoryService.updateFeatureStatus(feature).subscribe();
  }
}
