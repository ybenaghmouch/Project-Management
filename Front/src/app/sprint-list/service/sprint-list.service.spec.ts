import { TestBed } from '@angular/core/testing';

import { SprintListService } from './sprint-list.service';

describe('SprintListService', () => {
  let service: SprintListService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SprintListService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
