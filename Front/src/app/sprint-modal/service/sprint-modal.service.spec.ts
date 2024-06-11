import { TestBed } from '@angular/core/testing';

import { SprintModalService } from './sprint-modal.service';

describe('SprintModalService', () => {
  let service: SprintModalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SprintModalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
