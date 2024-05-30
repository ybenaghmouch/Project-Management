import { TestBed } from '@angular/core/testing';

import { RoleListService } from './role-list.service';

describe('RoleListService', () => {
  let service: RoleListService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoleListService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
