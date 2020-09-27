import { Component, OnInit } from '@angular/core';
import { ModifyingFlowService } from 'src/app/service/modifying-flow.service';

@Component({
  selector: 'app-design',
  templateUrl: './design.component.html',
  styleUrls: ['./design.component.css']
})
export class DesignComponent implements OnInit {

  constructor(
    private modifyingFlow: ModifyingFlowService
  ) { }

  ngOnInit(): void {
  }

  public saveAsNew(): void {
    this.modifyingFlow.saveAsNew();
  }



}
