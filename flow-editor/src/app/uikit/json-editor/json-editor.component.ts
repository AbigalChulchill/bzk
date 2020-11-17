import { Component, Input, OnInit } from '@angular/core';
import { CommUtils } from 'src/app/utils/comm-utils';
declare var jquery: any;
declare let $: any;
declare let JSONEditor: any;

@Component({
  selector: 'app-json-editor',
  templateUrl: './json-editor.component.html',
  styleUrls: ['./json-editor.component.css']
})
export class JsonEditorComponent implements OnInit {

  @Input() data: JsonDataProvide;
  public varJsonEditorId = CommUtils.makeAlphanumeric(16);

  constructor() { }

  async ngOnInit(): Promise<void> {
    await CommUtils.delay(10);
    this.setupJsonEditor();
  }

  private setupJsonEditor(): void {
    const container = document.getElementById(this.varJsonEditorId);
    const options = {
      onChangeJSON: (json) => {
        this.data.setDataJson(JSON.stringify(json));
      }
    };
    const editor = new JSONEditor(container, options);

    // set json
    const initialJson = this.data.getDataJson();
    if (!initialJson) { return; }
    editor.set(JSON.parse(initialJson));
  }

}

export interface JsonDataProvide {

  getDataJson(): string;

  setDataJson(d: string): void;

}


export class ReadJsonProvide implements JsonDataProvide {

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

  getDataJson(): string {
    return this.jsonText;
  }
  setDataJson(d: string): void { }

}
