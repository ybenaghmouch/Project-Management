import { Routes,RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { UserListComponent } from './user-list/user-list.component';
import { TeamListComponent } from './team-list/team-list.component';
export const routes: Routes = [
    { path: 'users', component: UserListComponent },
    { path: 'teams', component: TeamListComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  export class AppRoutingModule { }