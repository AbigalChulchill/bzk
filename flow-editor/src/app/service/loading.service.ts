import { Injectable } from '@angular/core';
declare var jquery: any;
declare let $: any;
@Injectable({
  providedIn: 'root'
})
export class LoadingService {

  private token: number;

  constructor() { }


  public twinkle(): void {
    const t = this.show();
    this.dismiss(t);
  }

  public show(): number {
    this.token = Math.floor((Math.random() * 1000000) + 1);
    $('#preloader').show();
    return this.token;
    // this.dismissLoading();
  }

  public dismiss(to: number): void {
    // Preloader
    if (to !== this.token) { return; }
    const lv =  $('#preloader');
    lv.delay(168).fadeOut('slow', () => {
      // $(this).remove();
    });
  }
}
