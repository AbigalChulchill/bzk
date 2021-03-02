import { ModifyingFlowService } from './../service/modifying-flow.service';
import { StringUtils } from './../utils/string-utils';
import { ConsoleDtos, BoxRunLog, ActionRunLog } from './../dto/console-dtos';
import { LoadingService } from './../service/loading.service';
import { CommUtils } from './../utils/comm-utils';
import { HttpClientService } from './../service/http-client.service';
import { environment } from 'src/environments/environment';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { state } from '@angular/animations';
import { RxStompService } from '@stomp/ng2-stompjs';

// declare let $: any;
// declare let Stomp: any;
// declare let SockJS: any;
//https://stomp-js.github.io/guide/ng2-stompjs/ng2-stomp-with-angular7.html
@Component({
  selector: 'app-console',
  templateUrl: './console.component.html',
  styleUrls: ['./console.component.css']
})
export class ConsoleComponent implements OnInit {
  @ViewChild('listlineDom') private listlineDom: ElementRef;
  ConsoleDtos = ConsoleDtos;
  LineType = LineType;
  StringUtils = StringUtils;
  lineList = new Array<Row>();
  keepReading = false;

  constructor(
    public httpClient: HttpClientService,
    private loading: LoadingService,
    private modifyingFlow: ModifyingFlowService,
    private rxStompService: RxStompService
  ) { }

  async ngOnInit(): Promise<void> {
    // const socket = new SockJS(environment.console.host + 'bzk-websocket');
    // this.stompClient = Stomp.over(socket);

    // const header = {
    //   Authorization: 'Basic token'
    //   }

    // this.stompClient.connect(header, (frame) => {
    //   console.log('Connected: ' + frame);
    //   this.stompClient.subscribe('/topic/tail', (m) => {
    //     console.log(m);
    //     this.lineList.push(this.parseByLine(m.body));
    //     this.scrollToBottom();
    //   });
    //   this.startRead();

    // });

    this.rxStompService.watch('/topic/tail',{
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Credentials': 'true',
      'Access-Control-Allow-Headers': 'Access-Control-Allow-Methods,Access-Control-Allow-Credentials,Access-Control-Allow-Origin,Access-Control-Allow-Headers,Authorization,Accept,Origin,DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range',
      'Access-Control-Allow-Methods': 'GET, PUT, POST, DELETE, OPTIONS, PATCH',
      'Access-Control-Max-Age': '86400',
      'Authorization': sessionStorage.getItem('token')
    }).subscribe(m => {
      console.log(m);
      this.lineList.push(this.parseByLine(m.body));
      this.scrollToBottom();
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
    this.rxStompService.publish({ destination: '/app/tail' });
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

    return new TextRow(l);
  }



  public listResult(): Array<Row> {
    return this.lineList;
  }



}

export enum LineType {
  Text,
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


