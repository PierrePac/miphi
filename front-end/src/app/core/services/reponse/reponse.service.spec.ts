import { TestBed } from '@angular/core/testing';

import { ReponseService } from './reponse.service';

describe('ReponseServiceService', () => {
  let service: ReponseService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReponseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
