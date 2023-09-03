import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { SandboxService } from 'src/app/core/services/sandbox/sandbox.service';
import { SandboxDto } from 'src/app/share/dtos/sandbox/sandbox-dto';

@Component({
  selector: 'app-list-sandbox',
  templateUrl: './list-sandbox.component.html',
  styleUrls: ['./list-sandbox.component.scss']
})
export class ListSandboxComponent implements OnInit{
  allSandbox$: Observable<SandboxDto[]> = of([]);
  sandboxes: SandboxDto[] =[]
  selectedSandbox!: SandboxDto | undefined;



  constructor(private sandboxService: SandboxService,) {
    this.allSandbox$ = this.sandboxService.sandbox$;
  }
  ngOnInit(): void {
    this.sandboxService.getAllSandbox().subscribe((data: SandboxDto[]) => {
      this.sandboxes = data;

    });
  }

  loadSandbox(sandbox: SandboxDto) {
    this.selectedSandbox = sandbox;
  }

}
