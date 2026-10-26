import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, provideHttpClientTesting } from '@angular/common/http/testing';
import { HttpTestingController } from '@angular/common/http/testing';
import { ProjectService } from '../services/project';
import { provideHttpClient } from '@angular/common/http';


describe('ProjectService', () => {
  let service: ProjectService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ProjectService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should fetch projects', () => {
    service.list().subscribe(projects => expect(projects.length).toBe(1));

    const req = httpMock.expectOne('/api/v1/projects');
    expect(req.request.method).toBe('GET');
    req.flush([{ id: 1, name: 'DevPulse', description: null, repoUrl: null, createdAt: '2026-10-11T20:29:50Z' }]);
  });

  it('should create a project', () => {
    const newProject = { id: 2, name: 'New Project', description: 'Description', repoUrl: 'https://github.com/user/repo' };
    service.create({ name: 'New Project', description: 'Description', repoUrl: 'https://github.com/user/repo' }).subscribe(project => expect(project).toEqual(newProject));

    const req = httpMock.expectOne('/api/v1/projects');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ name: 'New Project', description: 'Description', repoUrl: 'https://github.com/user/repo' });
    req.flush(newProject);
  });
  it('should update a project', () => {
    const updatedProject = { id: 1, name: 'Updated Project', description: 'Updated Description', repoUrl: 'https://github.com/user/updated-repo' };
    service.update(1, { name: 'Updated Project', description: 'Updated Description', repoUrl: 'https://github.com/user/updated-repo' }).subscribe(project => expect(project).toEqual(updatedProject));

    const req = httpMock.expectOne('/api/v1/projects/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual({ name: 'Updated Project', description: 'Updated Description', repoUrl: 'https://github.com/user/updated-repo' });
    req.flush(updatedProject);
  });

  it('should delete a project', () => {
    service.delete(1).subscribe(() => { });

    const req = httpMock.expectOne('/api/v1/projects/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

});
