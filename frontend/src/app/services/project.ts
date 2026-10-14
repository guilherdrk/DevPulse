import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import type { CreateProjectRequest, Project } from '../models/project';

@Injectable({
    providedIn: 'root',
})
export class ProjectService {
    private http = inject(HttpClient);

    list(): Observable<Project[]> {
        return this.http.get<Project[]>('/api/v1/projects');
    }

    create(request: CreateProjectRequest): Observable<Project> {
        return this.http.post<Project>('/api/v1/projects', request)
    }
}
