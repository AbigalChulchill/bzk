import { JSUtils } from './jsutils';
declare let mermaid: any;
declare var jquery: any;
declare let $: any;
// https://mermaid-js.github.io/mermaid/diagrams-and-syntax-and-examples/flowchart.html#subgraphs

export class ChartUtils {


  public static initEnv(): void {
    mermaid.mermaidAPI.initialize({
      startOnLoad: false,
      securityLevel: 'loose'
    });
  }

  public static build(domSelector: string): ChartBuilder {
    return new ChartBuilder(domSelector);
  }



  public static drawByCode(domSel: string, code: string): void {
    const insertSvg = (svgCode, bindFunctions) => {
      $(domSel).html(svgCode);
    };
    const graph = mermaid.mermaidAPI.render('graph', code, insertSvg);
  }

}

export class ChartBuilder {
  //
  subgraphs = new Array<Subgraph>();
  private domSelector: string;
  public static TEMPLATE = (boxContents, links) => `flowchart TB; \n ${boxContents} \n ${links}`;

  public constructor(ds: string) {
    this.domSelector = ds;
  }

  public addSubgraph(s: Subgraph): ChartBuilder {
    this.subgraphs.push(s);
    return this;
  }

  public genCode(): string {
    return ChartBuilder.TEMPLATE(this.genSubgraphsCode(), this.getLinksCode());
  }

  public getLinksCode(): string {
    let ans = '';
    for (const sq of this.subgraphs) {
      ans += sq.getLinksCode();
    }
    return ans;
  }

  public draw(): void {
    const code = this.genCode();
    ChartUtils.drawByCode(this.domSelector, code);
    this.injectClick();
  }

  private genSubgraphsCode(): string {
    let ans = '';
    for (const s of this.subgraphs) {
      ans += s.genCode();
    }
    return ans;
  }

  public injectClick(): void {
    this.subgraphs.forEach(s => s.injectClick());
  }

  public getSubgraph(id: string): Subgraph {
    return this.subgraphs.find(s => s.id === id);
  }

  public getNode(id: string): Node {
    for (const subg of this.subgraphs) {
      for (const n of subg.node.listDeepChildren()) {
        if (n.id === id) { return n; }
      }
    }
    throw new Error('It`s null node id:' + id);
  }


}

// box-link https://mermaid-js.github.io/mermaid/diagrams-and-syntax-and-examples/flowchart.html#subgraphs
// try box click https://jsfiddle.net/s37cjoau/3/
export class Subgraph {

  public name: string;
  public id: string;
  private click: (e: ChartClickInfo) => void;
  public node: Node;
  public static TEMPLATE = (name, content, styles) => `subgraph ${name}; \n ${content} \n ${styles} end; \n`;

  public constructor(i: string, n: string, cl: (e: ChartClickInfo) => void) {
    this.name = n;
    this.id = i;
    this.click = cl;
  }

  public getLinksCode(): string {
    let ans = '';
    for (const n of this.node.listDeepChildren()) {
      if (!n.subgraph) { continue; }
      ans += n.getLinkCode();
    }
    return ans;
  }

  public setNode(n: Node): Subgraph {
    this.node = n;
    return this;
  }

  public genCode(): string {
    let stes = '';
    for (const n of this.node.listDeepChildren()) {
      stes += n.genStyleCode();
    }
    return Subgraph.TEMPLATE(this.name, this.node.genCode(), stes);
  }

  public injectClick(): void {
    this.node.listDeepChildren().forEach(n => n.injectClick());
    $(`.cluster div:contains(${this.name})`).parents('.cluster').click(e => {
      this.click({
        event: e,
        srcId: this.id,
        srcName: this.name
      });
    });
  }


}

// click callback  https://mermaid-js.github.io/mermaid/diagrams-and-syntax-and-examples/flowchart.html#subgraphs
// js call angualr https://www.c-sharpcorner.com/blogs/call-angular-2-function-from-javascript
export class Node {


  private parents = null;
  public id: string;
  public name: string;
  public style: string;
  public subgraph: Subgraph;
  private click: (e: ChartClickInfo) => void;
  public children = new Array<Node>();
  public static TEMPLATE = (start, end, childrenContent) => ` ${start} --> ${end};  \n ${childrenContent} `;
  public genStyleCode = () => this.style ? `style ${this.name} ${this.style}; \n` : '';

  public constructor(i: string, n: string, ck: (e: ChartClickInfo) => void) {
    this.id = i;
    this.name = n;
    this.click = ck;
  }

  public getLinkCode(): string {
    return `${this.name} --> ${this.subgraph.name} \n`;
  }

  public setStyle(st: string): Node {
    this.style = st;
    return this;
  }

  public addChild(n: Node): Node {
    this.children.push(n);
    n.parents = this;
    return this;
  }

  public setSubgraph(b: Subgraph): Node {
    this.subgraph = b;
    return this;
  }

  public genCode(): string {
    let ans = '';
    // let noneTo = true;
    for (const c of this.children) {
      ans += Node.TEMPLATE(this.name, c.name, c.genCode());
    }
    return ans;
  }

  public injectClick(): void {
    $(`.node div:contains(${this.name})`).parents('.node').click(e => {
      this.click({
        event: e,
        srcId: this.id,
        srcName: this.name
      });
    });
  }

  public listDeepChildren(): Array<Node> {
    const l = new Array<Node>();
    return this.recursiveChildren(l);
  }

  private recursiveChildren(l: Array<Node>): Array<Node> {
    l.push(this);
    for (const n of this.children) {
      n.recursiveChildren(l);
    }
    return l;
  }

  public getRoot(): Node {
    let n = this;
    while (n.parents) {
      n = n.parents;
    }
    return n;
  }

}

export class ChartClickInfo {
  public event: any;
  public srcId: string;
  public srcName: string;
}
