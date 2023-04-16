import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { PricingFeed } from '../pricing-feed/pricing-feed.model';
import { ApiResponse } from '../model/api-response.model';
import { PricingFeedId } from '../pricing-feed/pricing-feed-id.model';

@Injectable({
  providedIn: 'root'
})
export class PricingFeedService {

  private apiUrl = 'http://localhost:9191/api/v1/pricing-feed';

  constructor(private http: HttpClient) { }

  getCount() : Observable<ApiResponse<number>> {
    const url = `${this.apiUrl}/count`;
    return this.http.get<ApiResponse<number>>(url, this.getHttpHeaders())
      .pipe(
        catchError(error => {
          console.error(error);
          throw error;
        })
      );
  }

  findAll(page: number, size: number): Observable<ApiResponse<PricingFeed[]>> {
    const url = `${this.apiUrl}?page=${page}&size=${size}`;
    return this.http.get<ApiResponse<PricingFeed[]>>(url)
      .pipe(
        map(response => {
          response.data = response.data.map(dto => {
            return new PricingFeed(
              new PricingFeedId(dto.id?.storeId, dto.id?.sku),
              dto.productName,
              dto.price,
              dto.date
            );
          });
          return response;
        }),
        catchError(error => {
          console.error(error);
          throw error;
        })
      );
  }

  findById(storeId: string, sku: string): Observable<ApiResponse<PricingFeed>> {
    const url = `${this.apiUrl}/${storeId}/${sku}`;
    return this.http.get<ApiResponse<PricingFeed>>(url)
      .pipe(
        catchError(error => {
          console.error(error);
          throw error;
        })
      );
  }

  create(pricingFeed: PricingFeed): Observable<ApiResponse<PricingFeed>> {
    const url = `${this.apiUrl}`;
    return this.http.post<ApiResponse<PricingFeed>>(url, pricingFeed, this.getHttpHeaders())
      .pipe(
        catchError(error => {
          console.error(error);
          throw error;
        })
      );
  }

  update(storeId: string, sku: string, pricingFeed: PricingFeed): Observable<ApiResponse<PricingFeed>> {
    const url = `${this.apiUrl}/${storeId}/${sku}`;
    return this.http.put<ApiResponse<PricingFeed>>(url, pricingFeed, this.getHttpHeaders())
      .pipe(
        catchError(error => {
          console.error(error);
          throw error;
        })
      );
  }

  delete(storeId: string, sku: string): Observable<ApiResponse<string>> {
    const url = `${this.apiUrl}/${storeId}/${sku}`;
    return this.http.delete<ApiResponse<string>>(url, this.getHttpHeaders())
      .pipe(
        catchError(error => {
          console.error(error);
          throw error;
        })
      );
  }

  import(file: File): Observable<any> {
    const url = `${this.apiUrl}/import`;
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<any>(url, formData);
  }

  private getHttpHeaders(): { headers: HttpHeaders } {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return { headers };
  }
}
