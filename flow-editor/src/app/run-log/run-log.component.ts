import { RunLogClientService } from './../service/run-log-client.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-run-log',
  templateUrl: './run-log.component.html',
  styleUrls: ['./run-log.component.css']
})
export class RunLogComponent implements OnInit {

  constructor(
    private runLogClient:RunLogClientService
  ) { }

  async ngOnInit(): Promise< void> {
  }

}
