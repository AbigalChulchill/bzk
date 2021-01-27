import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { TextProvide } from 'src/app/infrastructure/meta';
import { map, startWith } from 'rxjs/operators';
import { VarService } from 'src/app/service/var.service';

@Component({
  selector: 'app-ref-text',
  templateUrl: './ref-text.component.html',
  styleUrls: ['./ref-text.component.css']
})
export class RefTextComponent implements OnInit {

  @Input() text: TextProvide;

  myControl = new FormControl();
  options: string[] = [];
  filteredOptions: Observable<string[]>;

  constructor(
    private varService: VarService
  ) { }


  ngOnInit(): void {
    this.options = this.varService.listAllKeys();
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );
  }

  public setTextProvider(tp: TextProvide): void {
    this.text = tp;
  }


  private _filter(value: string): string[] {
    if (!this.options) { return []; }
    const filterValue = value.toLowerCase();
    return this.options.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }

  public get refContent(): string {
    if (!this.text) return '';
    return this.text.getStr();
  }

  public set refContent(s: string) {
    if (!this.text) return;
    this.text.setStr(s);
  }

}


