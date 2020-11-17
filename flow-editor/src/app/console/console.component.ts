import { ConsoleDtos, BoxRunLog, ActionRunLog } from './../dto/console-dtos';
import { LoadingService } from './../service/loading.service';
import { CommUtils } from './../utils/comm-utils';
import { HttpClientService } from './../service/http-client.service';
import { environment } from 'src/environments/environment';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { state } from '@angular/animations';

declare let $: any;
declare let Stomp: any;
declare let SockJS: any;

@Component({
  selector: 'app-console',
  templateUrl: './console.component.html',
  styleUrls: ['./console.component.css']
})
export class ConsoleComponent implements OnInit {
  @ViewChild('listlineDom') private listlineDom: ElementRef;
  ConsoleDtos = ConsoleDtos;
  LineType = LineType;
  lineList = new Array<Row>();
  stompClient = null;
  keepReading = false;
  allLoged = false;

  constructor(
    public httpClient: HttpClientService,
    private loading: LoadingService
  ) { }

  async ngOnInit(): Promise<void> {
    const socket = new SockJS(environment.console.host + 'bzk-websocket');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, (frame) => {
      console.log('Connected: ' + frame);
      this.stompClient.subscribe('/topic/tail', (m) => {
        console.log(m);
        this.lineList.push(this.parseByLine(m.body));
        this.scrollToBottom();
      });
      this.startRead();

    });
    await this.refleshReading();
  }

  scrollToBottom(): void {
    const a = async () => {
      await CommUtils.delay(1);
      this.listlineDom.nativeElement.scrollTop = this.listlineDom.nativeElement.scrollHeight;
    };
    a();
  }

  public startRead(): void {
    this.stompClient.send('/app/tail', {}, {});
    this.keepReading = true;
  }

  public async refleshReading(): Promise<void> {
    const ri = await this.httpClient.isTralKeepReading().toPromise();
    this.keepReading = ri.keepReading;
  }

  public async stopRead(): Promise<void> {
    await this.httpClient.stopTralKeepReading().toPromise();
    await this.refleshReading();
  }

  public clear(): void {
    this.lineList = new Array<Row>();
  }

  public async delTrailFile(): Promise<void> {
    const t = this.loading.show();
    await this.httpClient.clearTrailFile().toPromise();
    this.clear();
    await this.refleshReading();
    this.loading.dismiss(t);
  }

  private parseByLine(l: string): Row {
    const bl = ConsoleDtos.parseBoxRunLog(l);
    if (bl) { return new BoxRunLogRow(bl); }
    const al = ConsoleDtos.parseActionRunLog(l);
    if (al) { return new ActionRunLogRow(al); }
    return new TextRow(l);
  }


  public toBoxRunLogRow(l: Row): BoxRunLogRow { return l as BoxRunLogRow; }
  public toActionRunLogRow(l: Row): BoxRunLogRow { return l as ActionRunLogRow; }

  public listResult(): Array<Row> {
    return this.allLoged ? this.lineList : this.lineList.filter(s => s.type === LineType.ActionLog || s.type === LineType.BoxLog);
  }



}

export enum LineType {
  Text, BoxLog, ActionLog
}

export class Row {
  public orgText: string;
  public type: LineType;

  public constructor(ot: string, t: LineType) {
    this.orgText = ot;
    this.type = t;
  }



}

export class TextRow extends Row {
  public constructor(ot: string) {
    super(ot, LineType.Text);
  }
}

export class BoxRunLogRow extends Row {

  public log: BoxRunLog;

  public constructor(l: BoxRunLog) {
    super(l.orgText, LineType.BoxLog);
    this.log = l;
  }

  public getHeadClass(): string {
    return 'btn-danger';
  }

}

export class ActionRunLogRow extends BoxRunLogRow {
  public constructor(l: ActionRunLog) {
    super(l);
    this.type = LineType.ActionLog;
  }

  public getHeadClass(): string {
    return 'btn-primary';
  }
}
