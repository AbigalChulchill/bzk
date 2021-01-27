import { TextProvide } from './../infrastructure/meta';
import { Injectable } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { CodeEditorComponent } from './code-editor/code-editor.component';

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  private dialogRef: MatDialogRef<any>;

  constructor(
    private dialog: MatDialog
  ) { }

  public open(dialogC: any): MatDialogRef<any> {
     this.dialogRef = this.dialog.open(dialogC);
     return this.dialogRef;
  }


  public openCodeEditor(tp:TextProvide): void {
    console.log('openCodeEditor');
    const df= this.dialog.open(CodeEditorComponent);
    const cec:CodeEditorComponent = df.componentInstance;
    cec.data = tp;
  }

}
