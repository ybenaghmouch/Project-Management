import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsModalComponent } from './us-modal.component';

describe('UsModalComponent', () => {
  let component: UsModalComponent;
  let fixture: ComponentFixture<UsModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
