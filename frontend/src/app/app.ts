import { Component, inject, signal } from '@angular/core';
import { PingService } from './services/ping';

@Component({
  imports: [],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {


  private pingService = inject(PingService);
  status = signal('carregando...');

  constructor() {
    this.pingService.ping().subscribe({
      next: res => this.status.set(res.status),
      error: () => this.status.set('indisponível'),
    });
  }
}
