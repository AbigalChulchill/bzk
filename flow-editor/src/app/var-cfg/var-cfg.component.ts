import { VarCfg } from './../model/var-cfg';
import { StringUtils } from './../utils/string-utils';
import { BaseVar } from './../infrastructure/meta';
import { plainToClass } from 'class-transformer';
import { VarCfgClientService } from './../service/var-cfg-client.service';
import { Component, OnInit } from '@angular/core';
import { TextProvide } from '../infrastructure/meta';
import { __assign } from 'tslib';

export  const UPDATEING_HEX = '<___UPDATE___>'
@Component({
  selector: 'app-var-cfg',
  templateUrl: './var-cfg.component.html',
  styleUrls: ['./var-cfg.component.css']
})
export class VarCfgComponent implements OnInit {
  StringUtils = StringUtils;
  public list = new Array<VarCfg>();
  public loading = true;

  constructor(
    private varCfgClient: VarCfgClientService
  ) { }

  async ngOnInit(): Promise<void> {
    await this.reflesh();
  }

  public createEmpty(): void {
    this.list.push(new VarCfg());
  }

  public async reflesh(): Promise<void> {
    this.list = new Array<VarCfg>();
    this.loading = true;
    this.list = await this.varCfgClient.listAll();
    this.loading = false;
  }

  public getTxtProvide(v: VarCfg): VarCfgTextProvide { return new VarCfgTextProvide(v); }

  public async createToServer(v: VarCfg): Promise<void> {
    this.list = new Array<VarCfg>();
    this.loading = true;
    await this.varCfgClient.create(v);
    await this.reflesh();
  }

  public async updateToServer(v: VarCfg): Promise<void> {
    this.list = new Array<VarCfg>();
    this.loading = true;
    await this.varCfgClient.save(v);
    await this.reflesh();
  }

  public async removeToServer(uid:string): Promise<void> {
    this.list = new Array<VarCfg>();
    this.loading = true;
    await this.varCfgClient.remove(uid).toPromise();
    await this.reflesh();
  }

  public isUpdating(vc: VarCfg): boolean {
    if (StringUtils.isBlank(vc.uid)) return false;
    return vc.sha256 === UPDATEING_HEX;
  }

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
