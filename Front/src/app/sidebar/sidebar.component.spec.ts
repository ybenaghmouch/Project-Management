import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebaComponent } from './sidebar.component';

describe('SidebaComponent', () => {
  let component: SidebaComponent;
  let fixture: ComponentFixture<SidebaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SidebaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
