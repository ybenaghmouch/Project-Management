import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgFor, NgClass } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, NgFor, NgClass,RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  menuItems = [
    { label: 'Board', icon: 'bi bi-clipboard2-data',link:'/board' },
    { label: 'Backlog', icon: 'bi bi-box-arrow-in-down' ,link:'/backlogs'},
    { label: 'Report', icon: 'bi bi-bar-chart',link:'#' },
    { label: 'Inbox', icon: 'bi bi-inbox' ,link:'#'},
    { label: 'Settings', icon: 'bi bi-gear' ,link:'#'},
    { label: 'Manage Users', icon: 'bi bi-people' ,link:'/users'},
    { label: 'Manage Teams', icon: 'bi bi-person-plus',link:'/teams' },
    { label: 'Manage Projects', icon: 'bi bi-briefcase' ,link:'/projects'},
    { label: 'Manage Leaves', icon: 'bi bi-airplane' ,link:'/leaves'},
    { label: 'Manage Roles', icon: 'bi bi-people-fill' ,link:'/roles'},
    { label: 'Manage Holidays', icon: 'bi bi-calendar' ,link:'/holidays'},
    { label: 'Sprints', icon: 'bi bi-people-fill' ,link:'/sprints'}
  ];
  selectedItem: number = -1;

  selectItem(index: number) {
    this.selectedItem = index;
  }
}
