import { VarCfgService } from './../../../service/var-cfg.service';
import { StringUtils } from './../../../utils/string-utils';
import { OType } from './../../bzk-obj';
import { BzkUtils } from './../../../utils/bzk-utils';
import { CommUtils } from './../../../utils/comm-utils';
import { Flow } from './../../flow';
import { Component, OnInit } from '@angular/core';
import { ClazzExComponent } from 'src/app/utils/prop-utils';
import { plainToClass } from 'class-transformer';
import { BaseVar, TextProvide } from 'src/app/infrastructure/meta';
import { Entry } from '../../entry';
import { SelectHandler } from 'src/app/var-cfg/var-cfg.component';
import { VarCfg } from '../../var-cfg';

declare var jquery: any;
declare let $: any;
declare let JSONEditor: any;

@Component({
  selector: 'app-flow',
  templateUrl: './flow.component.html',
  styleUrls: ['./flow.component.css']
})
export class FlowComponent implements OnInit, ClazzExComponent, TextProvide {
  StringUtils = StringUtils;
  data: any;
  public entryTypes = new Array<OType>();
  public varCfgs = new Array<IdxVarCfg>();
  constructor(
    private varCfgService:VarCfgService
  ) { }



  getStr(): string {
    const o = plainToClass(Object, this.flow.vars);
    return JSON.stringify(o);
  }

  setStr(d: string): void {
    const o = JSON.parse(d);
    this.flow.vars = plainToClass(BaseVar, o);
  }

  init(d: any, mi: any): void {
    this.data = d;
  }

  public get flow(): Flow { return this.data; }


  async ngOnInit(): Promise<void> {
    this.listEntryType();
  }

  public listEntryType(): void {
    const ecs = BzkUtils.listChilds(Entry);
    for (const ec of ecs) {
      const ot = BzkUtils.getOTypeInfo(ec);
      this.entryTypes.push(ot);
    }

  }

  public onEntryTypeChange(e): void {
    console.log(e);
    const ot = BzkUtils.getTypeByClazz(e);
    const ne = new ot();
    const orgEbUid = this.flow.entry.boxUid;
    this.flow.entry = ne;
    this.flow.entry.boxUid = orgEbUid;
    this.flow.entry.clazz = BzkUtils.getOTypeInfo(ot).clazz;

  }

  public getVarCfgTitle(vn:string):string{
    if(StringUtils.isBlank(vn)) return 'TODO select';
    const sv= this.varCfgService.get(vn);
    return sv.name;
  }


  public listVarCfgs():Array<IdxVarCfg>{
    if(!this.varCfgs){
      this.varCfgs = new Array<IdxVarCfg>();
      for(let i =0;i<this.flow.varCfgNames.length;i++){
        this.varCfgs.push(new IdxVarCfg(this.flow.varCfgNames,i));
      }
    }
    return this.varCfgs;
  }

  public createVarCfg():void{
    this.flow.varCfgNames.push('');
    this.varCfgs = null;
  }

  public removeVarCfg(vn:string):void{
    CommUtils.removeArray(this.flow.varCfgNames,e=> e===vn);
    this.varCfgs = null;
  }


}

export class IdxVarCfg implements SelectHandler{

  private idx:number;
  private list:Array<string>;

  public constructor(l:Array<string>,n:number){
    this.list = l;
    this.idx = n;
  }

  getSelectName(): string {
    return this.list[this.idx];
  }
  select(v: VarCfg): void {
    this.list[this.idx] = v.name;
  }

}
