import { Component } from '@angular/core';
import { ProjectList } from './projects/project-list/project-list';

@Component({
  imports: [ProjectList],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {

}
