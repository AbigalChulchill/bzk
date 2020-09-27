import { BzkObj } from './bzk-obj';

export class Action extends BzkObj {

  public name: string;
}



export class NodejsAction extends Action {
  public code: string;
  public installs: Set<string>;
  public dependencies: Map<string, string>;
  public devDependencies: Map<string, string>;
}

export class RestAction extends Action {

}
