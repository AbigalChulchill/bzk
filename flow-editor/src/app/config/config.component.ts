import { LocalStoreService } from './../service/local-store.service';
import { TimeZoneIds } from './../infrastructure/time-zone-ids';
import { ToastService } from './../service/toast.service';
import { GithubService } from './../service/github.service';
import { Component, OnInit } from '@angular/core';
import { ZonedDateTime } from '@js-joda/core';
import { Observable } from 'rxjs';
import { FormControl } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
  styleUrls: ['./config.component.css']
})
export class ConfigComponent implements OnInit {
  public gistPassSaved = false;
  public gistPass = '';

  myControl = new FormControl();
  options = TimeZoneIds.listIds();
  filteredOptions: Observable<string[]>;


  constructor(
    private githubService: GithubService,
    private toast: ToastService,
    public localStore: LocalStoreService,
  ) { }

  ngOnInit(): void {


    this.gistPass = this.githubService.encryptionPass;

    this.filteredOptions = this.myControl.valueChanges
      .pipe(
        startWith(''),
        map(value => this._filter(value))
      );

  }

  public saveGistPass(): void {
    this.githubService.encryptionPass = this.gistPass;
    if (this.gistPassSaved) {
      localStorage.setItem(GithubService.KEY_ENCRYPTION, this.gistPass);
      this.toast.twinkle({
        title: 'OK',
        msg: ''
      });
    }
  }



  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }



}
