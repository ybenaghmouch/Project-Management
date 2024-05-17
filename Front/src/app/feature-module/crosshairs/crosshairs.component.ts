import { Component,HostListener, NgZone } from '@angular/core';

@Component({
  selector: 'app-crosshairs',
  templateUrl: './crosshairs.component.html',
  styleUrls: ['./crosshairs.component.scss']
})

@HostListener('window: resize', ['$event'])
export class CrosshairsComponent  {

  public innerHeight!: string;
  getScreenHeight() {
    this.innerHeight = window.innerHeight + 'px';
  }

  constructor(private ngZone: NgZone) {
    window.onresize = () => {
      this.ngZone.run(() => {
        this.innerHeight = window.innerHeight + 'px';
      });
    };
    this.getScreenHeight();
  }




}
