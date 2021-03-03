import { ModifyingFlowService } from './../service/modifying-flow.service';
import { StringUtils } from './../utils/string-utils';
import { ConsoleDtos, BoxRunLog, ActionRunLog } from './../dto/console-dtos';
import { LoadingService } from './../service/loading.service';
import { CommUtils } from './../utils/comm-utils';
import { HttpClientService } from './../service/http-client.service';
import { environment } from 'src/environments/environment';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';


//https://stomp-js.github.io/guide/ng2-stompjs/ng2-stomp-with-angular7.html
//https://github.com/batiwo/spring-websocket-angular6
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

  constructor(
    public httpClient: HttpClientService,
    private loading: LoadingService,
    private modifyingFlow: ModifyingFlowService,
  ) { }

  async ngOnInit(): Promise<void> {
    await this.reflesh();
  }

  scrollToBottom(): void {
    const a = async () => {
      await CommUtils.delay(1);
      this.listlineDom.nativeElement.scrollTop = this.listlineDom.nativeElement.scrollHeight;
    };
    a();
  }


  public async reflesh(): Promise<void> {
    this.clear();
    const list = await this.httpClient.getTailContent().toPromise();
    for(const l of list){
      // console.log(m);
      this.lineList.push(this.parseByLine(l));
    }
    this.scrollToBottom();
  }


  public clear(): void {
    this.lineList = new Array<Row>();
  }

  public async delTrailFile(): Promise<void> {
    const t = this.loading.show();
    await this.httpClient.clearTrailFile().toPromise();
    this.clear();
    await this.reflesh();
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


