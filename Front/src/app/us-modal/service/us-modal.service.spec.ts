import { TestBed } from '@angular/core/testing';

import { UsModalService } from './us-modal.service';

describe('UsModalService', () => {
  let service: UsModalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsModalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
