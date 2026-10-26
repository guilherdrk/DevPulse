import { Component, effect, inject, input, output, signal } from '@angular/core';
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

  project = input<Project | null>(null);
  projectSaved = output<Project>();
  cancelled = output<void>();

  constructor() {
    effect(() => {
      const p = this.project();
      if (p) {
        this.form.patchValue({
          name: p.name,
          description: p.description ?? '',
          repoUrl: p.repoUrl ?? '',
        });
      } else {
        this.form.reset();
      }
    });
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    this.errorMessage.set(null);

    const editing = this.project();
    const request = this.form.getRawValue();
    const call$ = editing
      ? this.projectService.update(editing.id, request)
      : this.projectService.create(request);

    call$.subscribe({
      next: res => {
        this.projectSaved.emit(res);
        this.form.reset();
        this.submitting.set(false);
      },
      error: () => {
        this.errorMessage.set(editing ? 'Erro ao atualizar projeto' : 'Erro ao criar projeto');
        this.submitting.set(false);
      },
    });
  }

}

