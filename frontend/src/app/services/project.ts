import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import type { Project } from '../models/project';

@Injectable({
    providedIn: 'root',
})
export class ProjectService {
    private http = inject(HttpClient);

    list(): Observable<Project[]> {
        return this.http.get<Project[]>('/api/v1/projects');
    }
}
