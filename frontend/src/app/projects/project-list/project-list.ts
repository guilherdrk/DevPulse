import { Component, inject, signal } from '@angular/core';
import { ProjectService } from '../../services/project';
import { Project } from '../../models/project';

@Component({
  imports: [],
  selector: 'app-project-list',
  styleUrl: './project-list.css',
  templateUrl: './project-list.html',
})
export class ProjectList {
  private projectService = inject(ProjectService);

  projects = signal<Project[]>([]);
  loading = signal(true);
  error = signal(false);

  constructor() {
    this.projectService.list().subscribe({
      next: data => {
        this.projects.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.error.set(true);
      },
    });

  }
  addProject(project: Project) {
    this.projects.update(listaAtual => [project, ...listaAtual]);
  }

}
