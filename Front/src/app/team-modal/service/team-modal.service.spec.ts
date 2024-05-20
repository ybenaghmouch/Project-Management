import { TestBed } from '@angular/core/testing';

import { TeamModalService } from './team-modal.service';

describe('TeamModalService', () => {
  let service: TeamModalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TeamModalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
