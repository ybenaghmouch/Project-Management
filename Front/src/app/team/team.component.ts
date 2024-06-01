import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TeamService } from './service/team.service';
import { HttpClientModule } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common'; 
@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  standalone: true,
  imports: [CommonModule, HttpClientModule ,],
  providers:[TeamService,],
  styleUrls: ['./team.component.css']
})
export class TeamComponent implements OnInit {
  team: any;
  manager: any;
  chefprojet: any;
  collaborateurs: any[] = [];

  constructor(private route: ActivatedRoute, private teamService: TeamService) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const teamName = params['teamname'];
      if (teamName) {
        this.loadTeam(teamName);
      }
    });
  }

  loadTeam(teamName: string): void {
    this.teamService.getTeamByName(teamName).subscribe(data => {
      this.team = data;
      this.manager = this.team.manager;
      this.chefprojet = this.team.chefprojet;
      this.collaborateurs = this.team.collaborateurs;
    });
  }

  getAvatarUrl(civility: string): string {
    return civility === 'Mr.' ? 'assets/avatar1.png' : 'assets/avatar2.png';
  }
}
