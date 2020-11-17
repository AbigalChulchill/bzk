import { Constant } from './../infrastructure/constant';
import { VarsStore } from './vars-store';
import { BzkUtils } from './../utils/bzk-utils';
import { Type, plainToClass } from 'class-transformer';
import { Flow } from '../model/flow';

export class GistFileMap extends Map<string, GistFile>{ }

export class Gist {

  public static KEY_MAIN_GIST_EXTENSION = '.bzk';

  public id: string;
  @Type(() => GistFileMap)
  public files: GistFileMap;
  public created_at: Date;
  public updated_at: Date;

  public static getMainFile(g: Gist): GistFile {
    const ks = g.files.keys();
    for (const k of ks) {
      if (k.endsWith(Gist.KEY_MAIN_GIST_EXTENSION)) { return g.files.get(k); }
    }
    return null;
  }

  public getMainFile(): GistFile {
    const ans = Gist.getMainFile(this);
    return plainToClass(GistFile, ans);
  }

  public getVarsStore(): VarsStore {
    if (!this.files.has(Constant.VARS_STORE_NAME)) { return new VarsStore(); }
    const f = this.files.get(Constant.VARS_STORE_NAME);
    const o = JSON.parse(f.content);
    const ans = plainToClass(VarsStore, o);
    return ans ? ans : new VarsStore();
  }

}

export class GistFile {
  public filename: string;
  public content: string;

  public convertModel(): Flow {
    const o = JSON.parse(this.content);
    const ans = BzkUtils.fitClzz(Flow, o);
    return ans;
  }

}
