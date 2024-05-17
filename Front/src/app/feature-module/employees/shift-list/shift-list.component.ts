import { Component, OnInit } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { DataService, apiResultFormat, getShiftList, routes } from 'src/app/core/core.index';
import { FormBuilder, FormGroup } from "@angular/forms";
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-shift-list',
  templateUrl: './shift-list.component.html',
  styleUrls: ['./shift-list.component.scss']
})
export class ShiftListComponent implements OnInit {
  public routes = routes;
  public lstShiftList: Array<getShiftList> = [];
  public searchDataValue = '';
  public addRevenueForm!: FormGroup  ;
  public editRevenueForm!: FormGroup ;
  mytime: Date = new Date();
  myTime: Date = new Date();
  setTime: Date = new Date();
  zones: Date = new Date();
  setZone: Date = new Date();
  setzone: Date = new Date();
  Zones: Date = new Date();
  settime: Date = new Date();

  dataSource!: MatTableDataSource<getShiftList>;

  showTimePicker: Array<string> = []


  // pagination variables
  public lastIndex = 0;
  public pageSize = 10;
  public totalData = 0;
  public skip = 0;
  public limit: number = this.pageSize;
  public pageIndex = 0;
  public serialNumberArray: Array<number> = [];
  public currentPage = 1;
  public pageNumberArray: Array<number> = [];
  public pageSelection: Array<pageSelection> = [];
  public totalPages = 0;
  //** / pagination variables

  constructor(private formBuilder: FormBuilder,private data: DataService, private datePipe: DatePipe) {}

  ngOnInit(): void {
    this.getTableData();
  }

  private getTableData(): void {
    this.lstShiftList = [];
    this.serialNumberArray = [];

    this.data.getShiftList().subscribe((res: apiResultFormat) => {
      this.totalData = res.totalData;
      res.data.map((res: getShiftList, index: number) => {
        const serialNumber = index + 1;
        if (index >= this.skip && serialNumber <= this.limit) {
          res.id = serialNumber;
          this.lstShiftList.push(res);
          this.serialNumberArray.push(serialNumber);
        }
      });
      this.dataSource = new MatTableDataSource<getShiftList>(this.lstShiftList);
      this.calculateTotalPages(this.totalData, this.pageSize);
    });

 
  }

  public sortData(sort: Sort) {
    const data = this.lstShiftList.slice();

    /* eslint-disable @typescript-eslint/no-explicit-any */
    if (!sort.active || sort.direction === '') {
      this.lstShiftList = data;
    } else {
      this.lstShiftList = data.sort((a: any, b: any) => {
        const aValue = (a as any)[sort.active];
        const bValue = (b as any)[sort.active];
        return (aValue < bValue ? -1 : 1) * (sort.direction === 'asc' ? 1 : -1);
      });
    }
  }

  public searchData(value: string): void {
    this.dataSource.filter = value.trim().toLowerCase();
    this.lstShiftList = this.dataSource.filteredData;
  }

  public getMoreData(event: string): void {
    if (event === 'next') {
      this.currentPage++;
      this.pageIndex = this.currentPage - 1;
      this.limit += this.pageSize;
      this.skip = this.pageSize * this.pageIndex;
      this.getTableData();
    } else if (event === 'previous') {
      this.currentPage--;
      this.pageIndex = this.currentPage - 1;
      this.limit -= this.pageSize;
      this.skip = this.pageSize * this.pageIndex;
      this.getTableData();
    }
  }

  public moveToPage(pageNumber: number): void {
    this.currentPage = pageNumber;
    this.skip = this.pageSelection[pageNumber - 1].skip;
    this.limit = this.pageSelection[pageNumber - 1].limit;
    if (pageNumber > this.currentPage) {
      this.pageIndex = pageNumber - 1;
    } else if (pageNumber < this.currentPage) {
      this.pageIndex = pageNumber + 1;
    }
    this.getTableData();
  }

  public changePageSize(): void {
    this.pageSelection = [];
    this.limit = this.pageSize;
    this.skip = 0;
    this.currentPage = 1;
    this.getTableData();
  }

  private calculateTotalPages(totalData: number, pageSize: number): void {
    this.pageNumberArray = [];
    this.totalPages = totalData / pageSize;
    if (this.totalPages % 1 !== 0) {
      this.totalPages = Math.trunc(this.totalPages + 1);
    }
    for (let i = 1; i <= this.totalPages; i++) {
      const limit = pageSize * i;
      const skip = limit - pageSize;
      this.pageNumberArray.push(i);
      this.pageSelection.push({ skip: skip, limit: limit });
    }
  }

  toggleTimePcker(value: string): void {

    if (this.showTimePicker[0] !== value) {
      this.showTimePicker[0] = value
    } else {
      this.showTimePicker = []
    }
  }

  formatTime(date: any): any {
    const selectedDate: Date = new Date(date)
    return this.datePipe.transform(selectedDate, 'h:mm a')
  }
}
export interface pageSelection {
  skip: number;
  limit: number;
}