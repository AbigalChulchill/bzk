import { CommUtils } from './../../utils/comm-utils';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { TextProvide } from 'src/app/infrastructure/meta';
import { EditorComponent, MonacoEditorModule, NgxMonacoEditorConfig } from 'ngx-monaco-editor';
import { VarService } from 'src/app/service/var.service';


@Component({
  selector: 'app-code-editor',
  templateUrl: './code-editor.component.html',
  styleUrls: ['./code-editor.component.css']
})
export class CodeEditorComponent implements OnInit {

  @ViewChild(EditorComponent) editorView: EditorComponent;

  editorOptions = { theme: 'vs-dark', language: 'javascript' };
  // code: string= 'function x() {\nconsole.log("Hello world!");\n}';
  @Input() data: TextProvide;
  public languages = new Array<string>();

  constructor(
    private varService: VarService
  ) { }

  async ngOnInit(): Promise<void> {
    while (!(window as any).monaco || !this.editorView) { await CommUtils.delay(50); }
    monaco.languages.getLanguages().forEach(l => {
      this.languages.push(l.id);
    });
    this.setupLanuage();
  }

  public get code(): string {
    return this.data.getStr();
  }

  public set code(s: string) {
    this.data.setStr(s);
  }

  public onValChange(event: Event): void {
    this.setupLanuage();
  }

  private setupLanuage(): void {
    this.editorView.options = this.editorOptions;
    const cits: monaco.languages.CompletionItem[] = [];
    const ks = this.varService.listAllKeys();
    ks.forEach(k => cits.push(this.genComplatedItem(k)));
    monaco.languages.registerCompletionItemProvider(this.editorOptions.language, {
      provideCompletionItems: () => {
        return {
          items: cits
        };
      }
    });


  }

  private genComplatedItem(k: string): monaco.languages.CompletionItem {
    return {
      label: '${' + k + '}',
      kind: monaco.languages.CompletionItemKind.Text,
      insertText: '${' + k + '}'
    };
  }

}
