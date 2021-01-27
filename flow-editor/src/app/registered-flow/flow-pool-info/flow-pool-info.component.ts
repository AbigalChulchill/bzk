import { FlowPoolInfo } from './../../dto/flow-pool-info';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-flow-pool-info',
  templateUrl: './flow-pool-info.component.html',
  styleUrls: ['./flow-pool-info.component.css']
})
export class FlowPoolInfoComponent implements OnInit {

  @Input() public row: FlowPoolInfo;

  constructor() { }

  ngOnInit(): void {
  }

}
