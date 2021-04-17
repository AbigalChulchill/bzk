import { CommUtils } from './../../../../utils/comm-utils';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Prop, PropInfoArgs } from 'src/app/utils/prop-utils';
import { UnfoldInfo } from '../prop-row.component';

@Component({
  selector: 'app-prop-map',
  templateUrl: './prop-map.component.html',
  styleUrls: ['./prop-map.component.css']
})
export class PropMapComponent implements OnInit {
  @Output() messageEvent = new EventEmitter<UnfoldInfo>();
  @Input() prop: Prop;
  private pmap = new Map<string, Prop>();
  public toCreate : boolean = false;
  public createingKey = '';

  constructor() { }

  ngOnInit(): void {
    this.genMap();
  }

  public createItem(): void {
    this.toCreate = false;
    // const os: object = ;
    this.prop.val[this.createingKey] = CommUtils.clone(this.prop.info.child.newObj);
    this.prop.val = this.prop.val
    this.genMap();
  }


  public listKeys(): Array<string> {
    if (this.pmap) return Array.from(this.pmap.keys());
    return this.genMap();
  }

  public getCProp(key:string):Prop{
    return this.pmap.get(key);
  }

  private genMap(): Array<string> {
    const ans = new Map<string, Prop>();
    const o: object = this.prop.val;
    for (const key of Object.keys(o)) {
      const cp = this.genPropMapItem(o, key);
      ans.set(key, cp);
    }

    this.pmap = ans;
    return Array.from(this.pmap.keys());
  }

  private genPropMapItem(obj: object, key: string): Prop {
    const ans = new Prop();
    ans.field = key;
    ans.object = obj;
    ans.onChange = ()=>{
      this.prop.val = this.prop.val
    };
    const infoA: PropInfoArgs = this.prop.info.child;
    ans.info = CommUtils.clone( infoA);
    ans.info.title = key;
    return ans;
  }


  public removeItem(key: string): void {
    this.pmap = null;
    delete this.prop.val[key];
    this.prop.val = this.prop.val;
  }


  receiveOnUnfoldMessage($event): void {
    const uf: UnfoldInfo = $event;
    console.log(uf);
    this.messageEvent.emit(uf);
  }


}
