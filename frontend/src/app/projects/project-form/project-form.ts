import { Component, inject, output, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Project } from '../../models/project';
import { ProjectService } from '../../services/project';

@Component({
  imports: [ReactiveFormsModule],
  selector: 'app-project-form',
  styleUrl: './project-form.css',
  templateUrl: './project-form.html',
})
export class ProjectForm {
  private fb = inject(NonNullableFormBuilder);

  form = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(100)]],
    description: ['', Validators.maxLength(1000)],
    repoUrl: ['', [Validators.maxLength(255), Validators.pattern('https?:\\/\\/.*')]]
  })

  submitting = signal(false);
  errorMessage = signal<string | null>(null);

  projectCreated = output<Project>();
  projectService = inject(ProjectService);

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.errorMessage.set(null);
      return;
    } else {
      this.submitting.set(true);

      this.projectService.create(this.form.getRawValue()).subscribe({
        next: res => {
          this.projectCreated.emit(res);
          this.form.reset();
          this.submitting.set(false);
        }, error: err => {
          this.errorMessage.set('Ocorreu um erro ao salvar o projeto' + err);
          this.submitting.set(false);
        }
      })
    }
  }
}

