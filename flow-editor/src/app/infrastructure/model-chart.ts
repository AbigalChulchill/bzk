import { ModelUtils } from './../utils/model-utils';
import { StringUtils } from './../utils/string-utils';
import { Action } from './../model/action';
import { ChartBuilder, ChartClickInfo, ChartUtils, Node, Subgraph } from './../utils/chart-utils';
import { Flow } from './../model/flow';
import { ModelObservable } from '../model/model-observe';
import { Box, Link } from '../model/box';
import { CommUtils } from '../utils/comm-utils';
import { Transition } from '../model/transition';





export class ModelChart implements ModelObservable {

  private model: Flow;
  private chartBuilder: ChartBuilder;
  private domSelector: string;

  private objClicks = new Map<number, (ci: ChartClickInfo) => boolean>();



  public constructor(ds: string) {
    this.domSelector = ds;
  }

  onInit(m: Flow): void {
    ChartUtils.initEnv();
    this.model = m;
    this.refleshBuilder();
  }
  onUpdate(m: Flow): void {
    this.model = m;
    this.refleshBuilder();
  }

  public getClicks(): Map<number, (ci: ChartClickInfo) => boolean> { return this.objClicks; }

  public refleshBuilder(): void {
    this.chartBuilder = ChartUtils.build(this.domSelector);
    const eb = this.model.getEntryBox();
    const obs = this.model.boxs.filter(b => b.uid !== eb.uid);
    this.chartBuilder.addSubgraph(this.genSubgraph(eb));
    for (const b of obs) {
      this.chartBuilder.addSubgraph(this.genSubgraph(b));
    }
    this.setupBoxLinks();
    console.log(this.chartBuilder.genCode());
    this.chartBuilder.draw();

  }

  private setupBoxLinks(): void {
    const lks = ModelUtils.listAllLinks(this.model);
    for (const lk of lks) {
      if (StringUtils.isBlank(lk.transition.toBox)) { continue; }
      const subg = this.chartBuilder.getSubgraph(lk.transition.toBox);
      const n = this.chartBuilder.getNode(lk.uid);
      n.setSubgraph(subg);
    }
    const bks = this.model.boxs;
    for (const b of bks) {
      if (StringUtils.isBlank(b.transition.toBox)) { continue; }
      const tob = this.chartBuilder.getSubgraph(b.transition.toBox);
      const ob = this.chartBuilder.getSubgraph(b.uid);
      ob.toSubgraph = tob;
    }

  }


  private genSubgraph(b: Box): Subgraph {
    const ans = new Subgraph(b.uid, StringUtils.opt(b.name, b.uid), e => this.onClick(e));
    const rootN = this.genNode(b);
    ans.setNode(rootN);
    return ans;
  }

  private genNode(b: Box): Node {
    const entryed = b.uid === this.model.getEntryBox().uid
    let node: Node = null;
    const tks = b.listTask();
    for (let i = 0; i < tks.length; i++) {
      const t = tks[i];
      const mkFirst = entryed && i === 0;
      if (t instanceof Link) {
        const lk = t;
        node = this.addNode(node, lk.uid, StringUtils.opt(lk.name, lk.uid), mkFirst ? 'fill:#f9f,stroke:#333,stroke-width:4px,stroke-dasharray: 5 5' : 'fill:#f9f,stroke:#333,stroke-width:4px');
      } else if (t instanceof Action) {
        const act: Action = t;
        node = this.addNode(node, act.uid, StringUtils.opt(act.name, act.uid), mkFirst ? 'stroke-width:4px,stroke-dasharray: 5 5' : null);
      } else {
        throw new Error('not support:' + JSON.stringify(t));
      }
    }
    if (tks.length === 1) {
      const nn = new Node(b.uid, 'ADD_' + b.uid, e => this.onClick(e)).setStyle('fill:#fff,stroke-width:4px,stroke-dasharray: 5 5');
      node.addChild(nn);
    }
    return node.getRoot();
  }

  private addNode(p: Node, uid: string, name: string, style: string): Node {
    if (p) {
      const nn = new Node(uid, name, e => this.onClick(e)).setStyle(style);
      p.addChild(nn);
      return nn;
    } else {
      return new Node(uid, name, e => this.onClick(e)).setStyle(style);
    }
  }

  private onClick(ci: ChartClickInfo): void {
    // alert('Node:' + ci.srcId);
    const ks = this.sortKeys(this.objClicks);
    for (const k of ks) {
      if (this.objClicks.get(k)(ci)) { break; }
    }
  }



  private sortKeys(m: Map<number, any>): Array<number> {
    const ans = Array.from<number>(m.keys());
    return ans.sort();
  }

}
