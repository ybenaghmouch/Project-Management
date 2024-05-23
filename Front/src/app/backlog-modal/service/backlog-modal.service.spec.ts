import { TestBed } from '@angular/core/testing';

import { BacklogModalService } from './backlog-modal.service';

describe('BacklogModalService', () => {
  let service: BacklogModalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BacklogModalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
