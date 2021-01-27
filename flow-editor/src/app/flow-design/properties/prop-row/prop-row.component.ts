import { StringUtils } from './../../../utils/string-utils';
import { DialogService } from './../../../uikit/dialog.service';
import { CommUtils } from 'src/app/utils/comm-utils';
import { Prop, PropInfoArgs, PropType, PropUtils } from './../../../utils/prop-utils';
import { Component, ComponentFactoryResolver, EventEmitter, Input, OnInit, Output, ViewChild, ViewContainerRef } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { CustomDirective } from '../custom.directive';
import { AfterViewInit } from '@angular/core';
import { RefSetType } from 'src/app/utils/prop-ref-val';
import { ObjTextProvide } from 'src/app/infrastructure/meta';

@Component({
  selector: 'app-prop-row',
  templateUrl: './prop-row.component.html',
  styleUrls: ['./prop-row.component.css']
})
export class PropRowComponent implements OnInit, AfterViewInit {
  public ObjTextProvide = ObjTextProvide;
  @ViewChild(CustomDirective) dynamicComponentLoader: CustomDirective;
  @Input() prop: Prop;
  @Output() messageEvent = new EventEmitter<UnfoldInfo>();

  public PropType = PropType;

  myControl = new FormControl();
  options: string[] = [];
  filteredOptions: Observable<string[]>;

  constructor(
    private componenFactoryResolver: ComponentFactoryResolver,
    public dialog: DialogService
  ) { }


  ngOnInit(): void {
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );
  }

  async ngAfterViewInit(): Promise<void> {
    while (!this.prop) {
      await CommUtils.delay(100);
    }
    await CommUtils.delay(1);
    this.setCustumView();
    if (this.prop.info.autocompleteList) this.options = this.prop.info.autocompleteList;
    if (!StringUtils.isBlank(this.prop.info.autocompleteFuncKey)) {
      const f = PropUtils.getInstance().getExAutoText(this.prop.info.autocompleteFuncKey);
      this.options = f();
    }
  }

  private _filter(value: string): string[] {
    if (!this.options) { return []; }
    const filterValue = value.toLowerCase();
    return this.options.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }


  public isCurType(pt: PropType): boolean {
     if (this.prop.val === null) return false;
    if (this.prop.info.refInfo && this.prop.refVal.getType() === RefSetType.ByRef) return false;
    return this.prop.type === pt;
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


  private setCustumView(): void {

    if (this.prop.type !== PropType.Custom) { return; }
    if (this.prop.val==null) { return; }
    const viewContainerRef = this.dynamicComponentLoader.viewContainerRef;
    viewContainerRef.clear();
    if (!this.prop) { return; }
    if (!this.prop.info.customView) { return; }

    this.initCustumView(viewContainerRef);
  }

  private initCustumView(viewContainerRef:ViewContainerRef):void{
    const targetComponent = PropUtils.getInstance().getExViewClazz(this.prop.info.customView);
    const componentFactory = this.componenFactoryResolver.resolveComponentFactory(targetComponent);

    const componentRef = viewContainerRef.createComponent(componentFactory);
    componentRef.instance.init(this.prop.val, this.prop);
  }


}



export class UnfoldInfo {
  public key: string;
  public obj: object;
}
