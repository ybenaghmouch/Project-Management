import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UtilsService {
  private apiUrl = '/api/holidays/calculate-duration';

  constructor(private http: HttpClient) {}

  calculateDuration(startDate: string, endDate: string): Observable<number> {
    const formattedStartDate = new Date(startDate).toISOString().split('T')[0];
    const formattedEndDate = new Date(endDate).toISOString().split('T')[0];

    const payload = {
      startDate: formattedStartDate,
      endDate: formattedEndDate
    };

    return this.http.post<number>(this.apiUrl, payload);
  }
}
