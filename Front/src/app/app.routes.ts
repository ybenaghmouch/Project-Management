import { Routes,RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { UserListComponent } from './user-list/user-list.component';
import { TeamListComponent } from './team-list/team-list.component';
import { BacklogListComponent } from './backlog-list/backlog-list.component';
import { BacklogComponent } from './backlog/backlog.component';
import { CommonModule } from '@angular/common';
import { ProjectListComponent } from './project-list/project-list.component';
import { RoleListComponent } from './role-list/role-list.component';
import { TeamComponent } from './team/team.component';
import { TaskListComponent } from './task-list/task-list.component';
import { SprintListComponent } from './sprint-list/sprint-list.component';
import { BoardComponent } from './board/board.component';
import { CongeComponent } from './conge/conge.component';
export const routes: Routes = [
    { path: 'users', component: UserListComponent },
    { path: 'backlogs/:projectname', component: BacklogListComponent },
    { path: 'backlogs', component: BacklogListComponent },
    { path: 'backlog', component: BacklogComponent },
    { path: 'tasks', component: TaskListComponent },
    { path: 'projects', component: ProjectListComponent },
    { path: 'teams', component: TeamListComponent },
    { path: 'team/:teamname', component: TeamComponent },
    { path: 'roles', component: RoleListComponent },
    { path: 'sprints', component: SprintListComponent },
    { path: 'sprints/:projectname', component: SprintListComponent },
    { path: 'leaves', component: CongeComponent },
    { path: 'board', component: BoardComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes),CommonModule ],
    exports: [RouterModule]
  })
  export class AppRoutingModule { }
