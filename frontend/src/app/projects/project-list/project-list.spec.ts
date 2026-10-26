import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { ProjectList } from './project-list';
import { ProjectService } from '../../services/project';
import { Project } from '../../models/project';

describe('ProjectList', () => {
  let component: ProjectList;
  let fixture: ComponentFixture<ProjectList>;
  let projectService: ProjectService;

  const project: Project = {
    id: 1, name: 'Project 1', description: 'Description 1',
    repoUrl: 'https://github.com/project1', createdAt: '2026-10-11T20:29:50Z',
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectList],
      providers: [
        {
          provide: ProjectService,
          useValue: {
            list: vi.fn().mockReturnValue(of([])),
            delete: vi.fn(),
          },
        },
      ],
    }).compileComponents();

    projectService = TestBed.inject(ProjectService);
    fixture = TestBed.createComponent(ProjectList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should remove the project from the list after a successful delete', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);
    vi.mocked(projectService.delete).mockReturnValue(of(undefined));
    component.projects.set([project]);

    component.removeProject(project);

    expect(projectService.delete).toHaveBeenCalledWith(1);
    expect(component.projects()).toEqual([]);
    expect(component.removing()).toBeNull();
  });

  it('should keep the project and show an error when delete fails', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);
    vi.mocked(projectService.delete).mockReturnValue(throwError(() => new Error('fail')));
    component.projects.set([project]);

    component.removeProject(project);

    expect(component.projects()).toEqual([project]);
    expect(component.actionError()).toBe('Erro ao remover projeto');
  });

  it('should not delete when the user cancels the confirmation', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(false);
    component.projects.set([project]);

    component.removeProject(project);

    expect(projectService.delete).not.toHaveBeenCalled();
    expect(component.projects()).toEqual([project]);
  });

  it('should emit editRequested with the project', () => {
    const emitSpy = vi.spyOn(component.editRequested, 'emit');
    component.editProject(project);
    expect(emitSpy).toHaveBeenCalledWith(project);
  });

  it('should replace the project when upserting an existing id', () => {
    component.projects.set([project]);
    component.upsertProject({ ...project, name: 'Novo nome' });
    expect(component.projects()).toHaveLength(1);
    expect(component.projects()[0].name).toBe('Novo nome');
  });

  it('should prepend the project when upserting a new id', () => {
    component.projects.set([project]);
    const novo = { ...project, id: 2 };
    component.upsertProject(novo);
    expect(component.projects()).toEqual([novo, project]);
  });
});
