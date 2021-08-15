import { UrlParamsService } from 'src/app/service/url-params.service';
import { RunLogComponent } from './../run-log/run-log.component';
import { CloudBackupListComponent } from './cloud-backup-list/cloud-backup-list.component';
import { TextProvide } from './../infrastructure/meta';
import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { CodeEditorComponent } from './code-editor/code-editor.component';
import { ListLogType } from '../service/run-log-client.service';
import { ScrollStrategy } from '@angular/cdk/overlay';
import { JsonEditorComponent } from './json-editor/json-editor.component';

declare let $: any;

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  private dialogRef: MatDialogRef<any>;

  constructor(
    private dialog: MatDialog,
    private urlParams: UrlParamsService
  ) { }

  public open(dialogC: any): MatDialogRef<any> {
    this.dialogRef = this.dialog.open(dialogC);
    return this.dialogRef;
  }

  public openCodeEditor(tp: TextProvide): void {
    console.log('openCodeEditor');
    const df = this.dialog.open(CodeEditorComponent);
    const cec: CodeEditorComponent = df.componentInstance;
    cec.data = tp;
  }

  public openJsonViewer(tp: TextProvide): void {
    console.log('openJsonViewer');
    const df = this.dialog.open(JsonEditorComponent);
    const jsonC: JsonEditorComponent = df.componentInstance;
    jsonC.fullWidth = true;
    jsonC.setData(tp);
  }

  public openCloudBackup(impCB: () => void): void {
    const df = this.dialog.open(CloudBackupListComponent);
    const cbc: CloudBackupListComponent = df.componentInstance;
    cbc.onImportDoneAction = impCB;
  }

  public openRunLoag(newTab: boolean, uid: string, type: ListLogType): void {
    if (newTab) {
      const u = this.urlParams.genRunLogUrl(null, uid, type);
      window.open(u, "_blank");
      return;
    }
    const cfg = new MatDialogConfig();
    cfg.minHeight = 500;
    const df = this.dialog.open(RunLogComponent, cfg);
    const cbc: RunLogComponent = df.componentInstance;
    cbc.listType = type;
    cbc.queryUid = uid;
  }

  public openJob(jobUid: string): void {
    const u = this.urlParams.genJobUrl(jobUid);
    window.open(u, "_blank");
  }

  // cdk-overlay-1



}
