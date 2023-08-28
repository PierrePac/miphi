import { TestBed } from '@angular/core/testing';

import { ReponseCandidatService } from './reponse-candidat.service';

describe('ReponseCandidatService', () => {
  let service: ReponseCandidatService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReponseCandidatService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
