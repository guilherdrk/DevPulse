import { Component, inject } from '@angular/core';
import { ProjectList } from './projects/project-list/project-list';
import { ProjectForm } from './projects/project-form/project-form';

@Component({
  imports: [ProjectForm, ProjectList],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {

}
