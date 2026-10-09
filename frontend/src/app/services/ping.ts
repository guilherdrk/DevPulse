import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';

export interface PingResponse {
    status: string;
    timestamp: string;
}

@Service()
export class PingService {

    private http = inject(HttpClient);

    ping(): Observable<PingResponse> {
        return this.http.get<PingResponse>('/api/v1/ping');
    }

}
