import { Routes,RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { UserListComponent } from './user-list/user-list.component';
import { TeamListComponent } from './team-list/team-list.component';
import { BacklogListComponent } from './backlog-list/backlog-list.component';
import { BacklogComponent } from './backlog/backlog.component';
import { CommonModule } from '@angular/common'; // Import CommonModule

export const routes: Routes = [
    { path: 'users', component: UserListComponent },
    { path: 'backlogs', component: BacklogListComponent },
    { path: 'backlog', component: BacklogComponent },
    { path: 'teams', component: TeamListComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes),CommonModule ],
    exports: [RouterModule]
  })
  export class AppRoutingModule { }