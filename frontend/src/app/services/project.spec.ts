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

});
