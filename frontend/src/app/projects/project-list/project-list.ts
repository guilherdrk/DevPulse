import { Component, inject, output, signal } from '@angular/core';
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

  removing = signal<number | null>(null);
  actionError = signal<string | null>(null);

  editRequested = output<Project>();

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

  removeProject(project: Project) {
    if (!confirm(`Remover "${project.name}"?`)) return;
    this.removing.set(project.id);

    this.projectService.delete(project.id).subscribe({
      next: () => {
        this.projects.update(lista => lista.filter(p => p.id != project.id));
        this.removing.set(null);
      }, error: () => {
        this.removing.set(null);
        this.actionError.set('Erro ao remover projeto');
      }
    })
  }
  editProject(project: Project) {
    this.editRequested.emit(project);
  }

  upsertProject(project: Project) {
    this.projects.update(lista => {
      const existe = lista.some(p => p.id === project.id);
      return existe ? lista.map(p => p.id === project.id ? project : p) : [project, ...lista];
    });
  }
}
