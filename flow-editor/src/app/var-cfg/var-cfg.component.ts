import { FetchState, VarCfgService } from './../service/var-cfg.service';
import { VarCfg } from './../model/var-cfg';
import { StringUtils } from './../utils/string-utils';
import { BaseVar } from './../infrastructure/meta';
import { plainToClass } from 'class-transformer';
import { Component, Input, OnInit } from '@angular/core';
import { TextProvide } from '../infrastructure/meta';
import { __assign } from 'tslib';

export const UPDATEING_HEX = '<___UPDATE___>'
@Component({
  selector: 'app-var-cfg',
  templateUrl: './var-cfg.component.html',
  styleUrls: ['./var-cfg.component.css']
})
export class VarCfgComponent implements OnInit {
  StringUtils = StringUtils;
  FetchState = FetchState;

  @Input() selectHandler: SelectHandler;

  constructor(
    public varCfgService: VarCfgService
  ) { }

  async ngOnInit(): Promise<void> {
    await this.varCfgService.reflesh();
  }

  public createEmpty(): void {
    const v = new VarCfg();
    v.sha256 = UPDATEING_HEX;
    this.varCfgService.list.push(v);
  }

  public txtChange(v: VarCfg): void {
    v.sha256 = UPDATEING_HEX;
  }

  public getBgClass(i: number): string {
    const c = i % 2;
    return c === 0 ? 'bg-warning' : 'bg-light'
  }

  public getTxtClass(i: number): string {
    const c = i % 2;
    return c === 0 ? 'text-dark' : 'text-primary'
  }

  public isSelected(v: VarCfg): boolean {
    if (!this.selectHandler) return false;
    return this.selectHandler.getSelectName() === v.name;
  }


  public getTxtProvide(v: VarCfg): VarCfgTextProvide { return new VarCfgTextProvide(v); }


  public isUpdating(vc: VarCfg): boolean {
    if (StringUtils.isBlank(vc.name)) return true;
    return vc.sha256 === UPDATEING_HEX;
  }

}

export interface SelectHandler {
  getSelectName(): string;

  select(v: VarCfg): void;

}

export class VarCfgTextProvide implements TextProvide {

  private var: VarCfg;

  public constructor(v: VarCfg) {
    this.var = v;
  }

  getStr(): string {
    return JSON.stringify(this.var.content);
  }
  setStr(d: string): void {
    const ino = JSON.parse(d);
    this.var.content = plainToClass(BaseVar, ino);
    this.var.sha256 = UPDATEING_HEX;
  }

}
