import { Injectable } from '@angular/core';
declare var jquery: any;
declare let $: any;
@Injectable({
  providedIn: 'root'
})
export class SVGZoomService {
  private svgSel: string;
  private boxSel: string;
  private msStX: number;
  private msStY: number;
  private draging = false;
  private orgSvgWidth = 0;
  private orgSvgHeight = 0;

  constructor() { }

  public get jqSvg(): any { return $(this.svgSel); }
  public get jqBox(): any { return $(this.boxSel); }

  public setTar(se: string, scse: string): void {
    this.svgSel = se;
    this.boxSel = scse;
    this.setupEvent();
    // this.jqSvg.height(this.jqBox.height());
    // this.jqSvg.css('max-width',this.jqBox.width());
    this.fitCurWidth(()=>{});
    const or = this.getRect();
    this.orgSvgHeight = or.height;
    this.orgSvgWidth = or.width;
  }

  private setupEvent(): void {
    this.jqBox.mousedown(e => {
      this.msStX = e.pageX;
      this.msStY = e.pageY;
      this.draging = true;
    });
    this.jqBox.mousemove(e => {
      if (!this.draging) return;
      const dx = this.msStX - e.pageX;
      const dy = this.msStY - e.pageY;
      this.movePos(dx, dy);
      this.msStX = e.pageX;
      this.msStY = e.pageY;
    });
    $('body').mouseup(e => {
      this.draging = false;
    });

  }



  public fitCurWidth(act: () => void): void {
    const cr = this.getRect();
    act();
    this.jqSvg.height(this.jqBox.height());
    this.jqSvg.css('max-width',this.jqBox.width());
    this.setRect(cr);
  }


  public resetPosZoom(): void {

    this.setRect({
      height: this.orgSvgHeight,
      width: this.orgSvgWidth,
      x: 0,
      y: 0
    });
  }

  public zoomPlus(): void {
    const r = this.getRect();
    r.width *= 0.92;
   this.setRect(r);
  }

  public zoomMinus(): void {
    const r = this.getRect();
    r.width *= 1.05;
   this.setRect(r);
  }

  public movePos(dx: number, dy: number): void {
    const r = this.getRect();
    r.x += dx;
    r.y += dy;
    this.setRect(r);
  }

  public setRect(r: Rect): void {
    const vbst = `${r.x} ${r.y} ${r.width} ${r.height}`;
    this.jqSvg.attr('viewBox', vbst);
  }

  public getRect(): Rect {
    const vbstr: string = this.jqSvg.attr('viewBox');
    const vsss = vbstr.split(' ');
    const ans = new Rect();
    ans.x = parseFloat(vsss[0]);
    ans.y = parseFloat(vsss[1]);
    ans.width = parseFloat(vsss[2]);
    ans.height = parseFloat(vsss[3]);

    return ans;
  }


}

class Rect {
  public x: number;
  public y: number;
  public width: number;
  public height: number;
}
