import { Component, OnInit } from '@angular/core';
import { RegisteredFlow } from '../dto/registered-flow';
import { HttpClientService } from '../service/http-client.service';

@Component({
  selector: 'app-registered-flow',
  templateUrl: './registered-flow.component.html',
  styleUrls: ['./registered-flow.component.css']
})
export class RegisteredFlowComponent implements OnInit {

  public list: Array<RegisteredFlow>;
  // panelOpenState = false;
  constructor(
    private httpClient: HttpClientService
  ) { }

  async ngOnInit(): Promise<void> {
    this.list = await this.httpClient.listRegisters().toPromise();
  }

}
