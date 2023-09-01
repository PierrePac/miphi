import { Component, ViewChild } from '@angular/core';
import { CreateSandboxComponent } from '../../create-sandbox/create-sandbox.component';
import { SandboxDto } from 'src/app/share/dtos/sandbox/sandbox-dto';

@Component({
  selector: 'app-sandbox',
  templateUrl: './sandbox.component.html',
  styleUrls: ['./sandbox.component.scss']
})
export class SandboxComponent {

  public mode: 'list-sandbox' | 'create-sandbox' = 'create-sandbox';

  toggleMode(): void {
    this.mode = this.mode === 'create-sandbox' ? 'list-sandbox' : 'create-sandbox';
  }

  handleSave(sandboxFormValue: SandboxDto) {
    console.log(sandboxFormValue)
  }
}
