import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommUtils } from 'src/app/utils/comm-utils';
import { Prop, PropInfoArgs } from 'src/app/utils/prop-utils';
import { UnfoldInfo } from '../prop-row.component';

@Component({
  selector: 'app-prop-list',
  templateUrl: './prop-list.component.html',
  styleUrls: ['./prop-list.component.css']
})
export class PropListComponent implements OnInit {
  @Output() messageEvent = new EventEmitter<UnfoldInfo>();
  @Input() prop: Prop;
  private list: Array<Prop>;
  constructor() { }

  ngOnInit(): void {
  }

  public createListItem(): void {
    const os: Array<any> = this.prop.val;
    os.push( CommUtils.clone( this.prop.info.child.newObj));
    this.list = null;
  }


  public listListProps(): Array<Prop> {
    if (this.list) { return this.list; }
    return this.genListProps();
  }

  private genListProps(): Array<Prop> {
    const ans = new Array<Prop>();
    const os: Array<any> = this.prop.val;
    for (let i = 0; i < os.length; i++) {
      const cp = this.genPropListItem( os, i);
      ans.push(cp);
    }
    this.list = ans;
    return ans;
  }

  private genPropListItem( os: Array<any>, idx: number): Prop {
    const ans = new Prop();
    ans.field = idx ;
    ans.object = os;
    const infoA: PropInfoArgs = this.prop.info.child;
    ans.info = infoA;
    return ans;
  }


  public removeListItem(idx: number): void {
    this.list = null;
    const os: Array<any> = this.prop.val;
    os.splice(idx, 1);
  }


  receiveOnUnfoldMessage($event): void {
    const uf: UnfoldInfo = $event;
    console.log(uf);
    this.messageEvent.emit(uf);
  }

}
