declare var jquery: any;
declare let $: any;

export class JSUtils {

  public static appendCallAngularFunc(b: CallAngularDto): void {
    // tslint:disable-next-line: max-line-length
    let calTxt = `window.${b.angularComponentReference}.zone.run(() => { window.${b.angularComponentReference}.${b.loadAngularFunction}('${b.p}'); }); `;
    calTxt += `\n console.log('${b.p}'); `;
    JSUtils.appendAction(b.id, b.jsMethodName, calTxt);
  }

  public static appendAction(id: string, methodName: string, code: string): void {
    const insTxt = `var ${methodName} = function() { \n ${code}  \n }`;
    JSUtils.appendJs(id, insTxt);
  }

  public static appendJs(id: string, content: string): void {
    const insHtml = `<script id="${id}" > \n  ${content}   \n </script>`;
    $('body').append(insHtml);
  }

  public static appendSrcJs(id: string, attrContent: string, content: string): void {
    $(`#${id}`).remove();
    const insHtml = `<script id="${id}"  ${attrContent}  >   \n  ${content}   \n  </script>`;
    $('body').append(insHtml);
  }


}

export class CallAngularDto {
  public id: string;
  public jsMethodName: string;
  public angularComponentReference: string;
  public loadAngularFunction: string;
  public p: any;
}
