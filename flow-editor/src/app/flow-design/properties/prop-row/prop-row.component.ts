import { CommUtils } from 'src/app/utils/comm-utils';
import { Prop, PropInfoArgs, PropType, PropUtils } from './../../../utils/prop-utils';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-prop-row',
  templateUrl: './prop-row.component.html',
  styleUrls: ['./prop-row.component.css']
})
export class PropRowComponent implements OnInit {

  @Input() prop: Prop;
  @Output() messageEvent = new EventEmitter<UnfoldInfo>();

  private listPropMap = new Map<Prop, Array<Prop>>();


  myControl = new FormControl();
  options: string[] = [];
  filteredOptions: Observable<string[]>;

  constructor() { }

  ngOnInit(): void {
    this.options = this.prop.info.autocompleteList;
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );
  }

  private _filter(value: string): string[] {
    if (!this.options) { return []; }
    const filterValue = value.toLowerCase();
    return this.options.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }


  receiveOnUnfoldMessage($event): void {
    const uf: UnfoldInfo = $event;
    console.log(uf);
    this.messageEvent.emit(uf);
  }

  public listEnums(p: Prop): string[] {
    return PropUtils.getInstance().listPropEnums(p);
  }

  public onUnfold(key: string, obj: object): void {
    this.messageEvent.emit({
      key,
      obj
    });
  }

  private genListProps(p: Prop): Array<Prop> {
    const ans = new Array<Prop>();
    const os: Array<any> = p.val;
    for (let i = 0; i < os.length; i++) {
      const cp = this.genPropListItem(p, os, i);
      ans.push(cp);
    }
    this.listPropMap.set(p, ans);
    return ans;
  }


  public listListProps(p: Prop): Array<Prop> {
    if (this.listPropMap.has(p)) { return this.listPropMap.get(p); }
    return this.genListProps(p);
  }

  public createListItem(listRoot: Prop): void {
    this.listPropMap.delete(listRoot);
    const os: Array<any> = listRoot.val;
    os.push(listRoot.info.listChildNewObj);
  }

  public removeListItem(listRoot: Prop, idx: number): void {
    this.listPropMap.delete(listRoot);
    const os: Array<any> = listRoot.val;
    os.splice(idx, 1);
  }

  private genPropListItem(root: Prop, os: Array<any>, idx: number): Prop {
    const ans = new Prop();
    ans.field = idx + '';
    ans.object = root.val;
    const infoA: PropInfoArgs = CommUtils.clone(root.info);
    infoA.type = root.info.listChildType;
    infoA.title = idx + '';
    ans.info = infoA;
    ans.valGeter = () => os[idx];
    ans.valSeter = (o) => os[idx] = o;


    return ans;
  }

}



export class UnfoldInfo {
  public key: string;
  public obj: object;
}
