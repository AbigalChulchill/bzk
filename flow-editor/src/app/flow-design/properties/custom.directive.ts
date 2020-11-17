import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[appCustom]'
})
export class CustomDirective {

  constructor(public viewContainerRef: ViewContainerRef) { }

}
