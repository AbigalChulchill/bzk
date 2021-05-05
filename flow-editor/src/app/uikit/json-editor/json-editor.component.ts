import { TextProvide } from './../../infrastructure/meta';
import { ClazzExComponent } from './../../utils/prop-utils';
import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { CommUtils } from 'src/app/utils/comm-utils';
import { VarCfgService } from 'src/app/service/var-cfg.service';
declare var jquery: any;
declare let $: any;
declare let JSONEditor: any;

@Component({
  selector: 'app-json-editor',
  templateUrl: './json-editor.component.html',
  styleUrls: ['./json-editor.component.css']
})
export class JsonEditorComponent implements OnInit, ClazzExComponent, AfterViewInit {

  @Input() data: TextProvide;
  public varJsonEditorId = CommUtils.makeAlphanumeric(16);
  private mode = 'tree';
  public copyJson = '';
  public fullWidth = false;
  private exOrgObj: any;

  constructor(
    private varCfg: VarCfgService,
  ) { }

  async ngAfterViewInit(): Promise<void> {
    await CommUtils.delay(10);
    this.setupJsonEditor();
  }


  init(d: any, mataInfo: any): void {
    this.exOrgObj = d;
    this.data = new SimpleJsonProvide(this.exOrgObj);
  }


  async ngOnInit(): Promise<void> {

  }

  private setupJsonEditor(): void {
    $('#' + this.varJsonEditorId).empty();
    if (!this.data) return;
    console.log('setupJsonEditor');
    const container = document.getElementById(this.varJsonEditorId);
    const options = {
      onChangeJSON: (json) => {
        this.data.setStr(JSON.stringify(json));
        this.copyJson = this.data.getStr();
      },
      onChangeText: (t) => {
        try {
          this.data.setStr(t);
        } catch (ex) { console.warn(ex); }
        this.copyJson = t;
      },
      mode: this.mode
    };
    const editor = new JSONEditor(container, options);

    // set json
    const initialJson = this.data.getStr();
    if (!initialJson) { return; }
    editor.set(JSON.parse(initialJson));
    this.copyJson = initialJson;
  }

  public setMode(m: string): void {
    this.mode = m;
    this.setupJsonEditor();
  }

  public listVarKeys(): Array<string> {
    return this.varCfg.list.map(v => v.name);
  }

  public importData(cfgk: string): void {
    const cfg = this.varCfg.get(cfgk);
    const orgj = JSON.parse(this.data.getStr())
    let merged = { ...orgj, ...cfg.content };
    this.data.setStr(JSON.stringify(merged));
    this.setupJsonEditor();

  }

  public setData(dp: TextProvide): void {
    this.data = dp;
    // this.setupJsonEditor();
  }



}




export class ReadJsonProvide implements TextProvide {

  private jsonText: string;

  public static gen(ino: any): ReadJsonProvide {
    return new ReadJsonProvide(ino);
  }

  private constructor(o: any) {
    if (typeof o === 'string') {
      this.jsonText = o;
      return;
    }
    this.jsonText = JSON.stringify(o);
  }

  getStr(): string {
    return this.jsonText;
  }
  setStr(d: string): void { }

}

export class SimpleJsonProvide implements TextProvide {

  public obj: any;

  public static gen(ino: any): SimpleJsonProvide {
    return new SimpleJsonProvide(ino);
  }

  public constructor(o: any) {
    this.obj = o;

  }

  getStr(): string {
    return JSON.stringify(this.obj);
  }

  setStr(d: string): void {
    CommUtils.cleanObj(this.obj);
    const src = JSON.parse(d);
    CommUtils.addProps(this.obj, src);
    console.log(this.obj);
    console.log(d);
  }

}
