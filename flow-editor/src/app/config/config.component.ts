import { ToastService } from './../service/toast.service';
import { GithubService } from './../service/github.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
  styleUrls: ['./config.component.css']
})
export class ConfigComponent implements OnInit {
  public gistPassSaved = false;
  public gistPass = '';


  constructor(
    private githubService: GithubService,
    private toast: ToastService
  ) { }

  ngOnInit(): void {
    this.gistPass = this.githubService.encryptionPass;
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

}
