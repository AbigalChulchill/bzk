import { RunLogComponent } from './../run-log/run-log.component';
import { CloudBackupListComponent } from './cloud-backup-list/cloud-backup-list.component';
import { TextProvide } from './../infrastructure/meta';
import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { CodeEditorComponent } from './code-editor/code-editor.component';
import { ListLogType } from '../service/run-log-client.service';
import { ScrollStrategy } from '@angular/cdk/overlay';

declare let $: any;

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

  public openCloudBackup(impCB:()=>void):void{
    const df= this.dialog.open(CloudBackupListComponent);
    const cbc:CloudBackupListComponent = df.componentInstance;
    cbc.onImportDoneAction = impCB;
  }

  public openRunLoag(uid:string,type:ListLogType):void{
    const cfg = new MatDialogConfig();
    cfg.minHeight = 500;
    const df= this.dialog.open(RunLogComponent,cfg);
    const cbc:RunLogComponent = df.componentInstance;
    cbc.listType = type;
    cbc.queryUid = uid;

  }

  // cdk-overlay-1



}
