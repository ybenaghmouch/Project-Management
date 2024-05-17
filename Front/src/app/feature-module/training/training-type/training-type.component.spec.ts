import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrainingTypeComponent } from './training-type.component';

describe('TrainingTypeComponent', () => {
  let component: TrainingTypeComponent;
  let fixture: ComponentFixture<TrainingTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TrainingTypeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TrainingTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
