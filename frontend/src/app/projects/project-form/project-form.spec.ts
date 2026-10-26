import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { ProjectForm } from './project-form';
import { ProjectService } from '../../services/project';
import { Project } from '../../models/project';

describe('ProjectForm', () => {
  let component: ProjectForm;
  let fixture: ComponentFixture<ProjectForm>;
  let projectService: ProjectService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectForm],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ProjectForm);
    component = fixture.componentInstance;
    projectService = TestBed.inject(ProjectService);
    await fixture.whenStable();
  });

  // Teste 1: Estado inicial
  it('deve iniciar com o formulário inválido pois o campo name é obrigatório', () => {
    expect(component.form.valid).toBe(false) // false ou true?
    expect(component.form.controls.name.hasError('required')).toBe(true);
  });

  // Teste 2: Validação de URL
  it('deve invalidar o campo repoUrl se não começar com http:// ou https://', () => {
    const repoControl = component.form.controls.repoUrl;

    repoControl.setValue('ftp://invalido.com');
    expect(repoControl.hasError('pattern')).toBe(true);

    repoControl.setValue('https://github.com/usuario/repo');
    expect(repoControl.hasError('pattern')).toBe(false); // o que esperamos quando a URL é válida?
  });

  // Teste 3: Submissão bloqueada se inválido
  it('não deve chamar projectService.create se o formulário estiver inválido', () => {
    // Criamos um espião (spy) no método create do serviço
    const createSpy = vi.spyOn(projectService, 'create');

    component.onSubmit();

    expect(createSpy).not.toHaveBeenCalled();
    expect(component.form.controls.name.touched).toBe(true); // confirmando se marcou como touched
  });

  // Teste 4: Submissão com sucesso
  it('deve chamar projectService.create e emitir evento ao salvar com sucesso', () => {
    const mockCreatedProject: Project = {
      id: 1,
      name: 'Novo Projeto',
      description: 'Desc',
      repoUrl: 'https://github.com/teste',
      createdAt: '2026-10-14T10:00:00Z',
    };

    // Ensinamos o spy a devolver um Observable de sucesso:
    vi.spyOn(projectService, 'create').mockReturnValue(of(mockCreatedProject));

    // Espionamos a emissão do output projectCreated
    const emitSpy = vi.spyOn(component.projectSaved, 'emit');

    // Preenchemos o formulário com dados válidos:
    component.form.setValue({
      name: 'Novo Projeto',
      description: 'Desc',
      repoUrl: 'https://github.com/teste',
    });


    component.onSubmit();

    expect(projectService.create).toHaveBeenCalledWith({
      name: 'Novo Projeto',
      description: 'Desc',
      repoUrl: 'https://github.com/teste',
    });
    expect(emitSpy).toHaveBeenCalledWith(mockCreatedProject);
    expect(component.submitting()).toBe(false);
  });

  it('should update a project', () => {
    const mockUpdatedProject: Project = {
      id: 1,
      name: 'Updated Project',
      description: 'Updated Description',
      repoUrl: 'https://github.com/updated-repo',
      createdAt: '2026-10-14T10:00:00Z',
    };

    vi.spyOn(projectService, 'update').mockReturnValue(of(mockUpdatedProject));
    const emitSpy = vi.spyOn(component.projectSaved, 'emit');

    component.form.setValue({
      name: 'Updated Project',
      description: 'Updated Description',
      repoUrl: 'https://github.com/updated-repo',
    });
    fixture.componentRef.setInput("project", mockUpdatedProject);
    component.onSubmit();

    expect(projectService.update).toHaveBeenCalledWith(1, {
      name: 'Updated Project',
      description: 'Updated Description',
      repoUrl: 'https://github.com/updated-repo',
    });
    expect(emitSpy).toHaveBeenCalledWith(mockUpdatedProject);
    expect(component.submitting()).toBe(false);
  });



});
