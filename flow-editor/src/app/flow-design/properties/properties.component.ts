import { CustomDirective } from './custom.directive';
import { UnfoldInfo } from './prop-row/prop-row.component';
import { CurProperties, Source } from './cur-properties';
import { LanguageService } from './../../service/language.service';
import { Component, ComponentFactoryResolver, OnInit, ViewChild } from '@angular/core';
import { PropType, PropUtils } from 'src/app/utils/prop-utils';

@Component({
  selector: 'app-properties',
  templateUrl: './properties.component.html',
  styleUrls: ['./properties.component.css']
})
export class PropertiesComponent implements OnInit {

  private static instance: PropertiesComponent;
  @ViewChild(CustomDirective) dynamicComponentLoader: CustomDirective;
  public PropType: PropType;
  private curProperties = new CurProperties(() => this.setExClazzView());

  public static getInstance(): PropertiesComponent { return PropertiesComponent.instance; }

  constructor(
    private componenFactoryResolver: ComponentFactoryResolver,
    private language: LanguageService
  ) { }

  ngOnInit(): void {
    PropertiesComponent.instance = this;
  }

  public getCurProperties(): CurProperties { return this.curProperties; }

  receiveOnUnfoldMessage($event): void {
    const uf: UnfoldInfo = $event;
    this.curProperties.nextTarget(uf);
  }

  public getCurClassTitle(): string {
    return this.getClassTitle(this.curProperties.source);
  }

  public getClassTitle(s: Source): string {
    if (!s) { return ''; }
    const ca = s.getTarClazzArgs();
    if (!ca) { return ''; }
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
    componentRef.instance.init(this.curProperties.source.target.obj);
  }


}
