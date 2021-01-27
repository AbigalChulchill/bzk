import { CommUtils } from './../../utils/comm-utils';
import { StringUtils } from './../../utils/string-utils';
import { CustomDirective } from './custom.directive';
import { UnfoldInfo } from './prop-row/prop-row.component';
import { CurProperties, Source } from './cur-properties';
import { LanguageService } from './../../service/language.service';
import { Component, ComponentFactoryResolver, Input, OnInit, ViewChild } from '@angular/core';
import { PropType, PropUtils } from 'src/app/utils/prop-utils';

@Component({
  selector: 'app-properties',
  templateUrl: './properties.component.html',
  styleUrls: ['./properties.component.css']
})
export class PropertiesComponent implements OnInit {

  @Input() public set initTar(o:any){
    this.waitSetTar(o);
  }

  @ViewChild(CustomDirective) dynamicComponentLoader: CustomDirective;
  public PropType: PropType;
  public curProperties = new CurProperties(() => this.setExClazzView());


  constructor(
    private componenFactoryResolver: ComponentFactoryResolver,
    private language: LanguageService
  ) { }

  ngOnInit(): void {
  }

  public getCurProperties(): CurProperties { return this.curProperties; }

  receiveOnUnfoldMessage($event): void {
    const uf: UnfoldInfo = $event;
    this.curProperties.nextTarget(uf);
  }



  private async waitSetTar(o:any):Promise<void>{
    while(!this.dynamicComponentLoader){ await CommUtils.delay(50); }
    this.curProperties.setTarget({
      key: '',
      obj: o
    });
  }

  public getCurClassTitle(): string {
    return this.getClassTitle(this.curProperties.source);
  }

  public getClassTitle(s: Source): string {
    if (!s) { return null; }
    const ca = s.getTarClazzArgs();
    if (!ca) { return null; }
    if (StringUtils.isBlank(ca.title)) { return null; }
    return s.target.key + ' (' + ca.title + ')';
  }

  setExClazzView(): void {
    const viewContainerRef = this.dynamicComponentLoader.viewContainerRef;
    viewContainerRef.clear();
    if (!this.curProperties.clazzInfo) { return; }
    if (!this.curProperties.clazzInfo.exView) { return; }
    const targetComponent = PropUtils.getInstance().getExViewClazz(this.curProperties.clazzInfo.exView);
    const componentFactory = this.componenFactoryResolver.resolveComponentFactory(targetComponent);

    const componentRef = viewContainerRef.createComponent(componentFactory);
    componentRef.instance.init(this.curProperties.source.target.obj, this.curProperties.source);
  }


}
