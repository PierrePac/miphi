import { Component } from '@angular/core';
import { SandboxDto } from 'src/app/share/dtos/sandbox/sandbox-dto';
import { SandboxService } from 'src/app/core/services/sandbox/sandbox.service';

@Component({
  selector: 'app-sandbox',
  templateUrl: './sandbox.component.html',
  styleUrls: ['./sandbox.component.scss']
})
export class SandboxComponent {
  public mode: 'list-sandbox' | 'create-sandbox' = 'create-sandbox';
  sandboxData!: SandboxDto;

  constructor(private sandboxService: SandboxService) {}

  toggleMode(): void {
    this.mode = this.mode === 'create-sandbox' ? 'list-sandbox' : 'create-sandbox';
  }

  handleSave(sandboxFormValue: SandboxDto) {
    this.sandboxData = sandboxFormValue;
    this.sandboxService.saveSandbox(this.sandboxData).subscribe((resp: SandboxDto) => {
      console.log(resp)
    })
  }
}
