import { CurPropertiesService } from './cur-properties.service';
import { Component, OnInit } from '@angular/core';
import { PropType } from 'src/app/utils/prop-utils';

@Component({
  selector: 'app-properties',
  templateUrl: './properties.component.html',
  styleUrls: ['./properties.component.css']
})
export class PropertiesComponent implements OnInit {

  public PropType: PropType;

  constructor(
    public curProperties: CurPropertiesService
  ) { }

  ngOnInit(): void {
  }

}
