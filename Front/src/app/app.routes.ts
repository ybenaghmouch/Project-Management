import { Routes,RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { UserListComponent } from './user-list/user-list.component';
import { TeamListComponent } from './team-list/team-list.component';
import { BacklogListComponent } from './backlog-list/backlog-list.component';
import { BacklogComponent } from './backlog/backlog.component';
import { CommonModule } from '@angular/common';
import { ProjectListComponent } from './project-list/project-list.component';
import { RoleListComponent } from './role-list/role-list.component';

export const routes: Routes = [
    { path: 'users', component: UserListComponent },
    { path: 'backlogs', component: BacklogListComponent },
    { path: 'backlog', component: BacklogComponent },
    { path: 'projects', component: ProjectListComponent },
    { path: 'teams', component: TeamListComponent },
    { path: 'roles', component: RoleListComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes),CommonModule ],
    exports: [RouterModule]
  })
  export class AppRoutingModule { }
