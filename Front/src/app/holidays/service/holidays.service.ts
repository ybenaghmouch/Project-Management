import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HolidayService {
  private apiUrl = '/api/holidays';

  constructor(private http: HttpClient) {}

  getAllHolidays(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  createHoliday(holiday: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, holiday);
  }

  updateHoliday(id: number, holiday: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, holiday);
  }

  deleteHoliday(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }

  calculateDuration(startDate: string, endDate: string): Observable<number> {
    const payload = {
      startDate,
      endDate
    };

    return this.http.post<number>(`${this.apiUrl}/calculate-duration`, payload);
  }
}
