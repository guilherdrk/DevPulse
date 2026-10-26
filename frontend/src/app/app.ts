import { Component, inject, signal } from '@angular/core';
import { ProjectList } from './projects/project-list/project-list';
import { ProjectForm } from './projects/project-form/project-form';
import { Project } from './models/project';

@Component({
  imports: [ProjectForm, ProjectList],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  editingProject = signal<Project | null>(null);

}
