import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Flow } from '../model/flow';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  constructor(
    private httpClient: HttpClient
  ) { }

  public getDemoModel(): Observable<Flow> {
    return this.httpClient.get<Flow>(environment.apiHost + '/flow/editor/demo');
  }

  public createGits(p: boolean, desc: string, fs: GistFile[]): Observable<Gist> {

    const data = {
      description: desc,
      public: p,
      files: {}
    };

    fs.forEach(f => {
      const fo = {
        content: f.content,
        type: 'application/json'
      };
      data.files[f.filename] = fo;
    });
    return this.httpClient.post<Gist>(environment.githubHost + 'gists', data);
  }

  public listGits(): Observable<Array<Gist>> {
    return this.httpClient.get<Array<Gist>>(environment.githubHost + 'gists');
  }

  public getGist(id: string): Observable<Gist> {
    return this.httpClient.get<Gist>(environment.githubHost + 'gists/' + id);
  }

}

export class Gist {
  public id: string;
  public files: Map<string, GistFile>;
  public created_at: Date;
  public updated_at: Date;
}

export class GistFile {
  public filename: string;
  public content: string;

}


