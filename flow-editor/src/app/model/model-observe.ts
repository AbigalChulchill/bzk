import { Flow } from './flow';
export class ModelObserve {
  private observables = new Array<ModelObservable>();
  private model: Flow;

  public setModel(f: Flow): void {
    this.model = f;
  }

  public getModel(): Flow {
    return this.model;
  }

  public addObservable(mo: ModelObservable): void {
    this.observables.push(mo);
    mo.onInit(this.model);
  }

  public notifyAll(): void {
    this.observables.forEach(o => o.onUpdate(this.model));
  }

}

export interface ModelObservable {
  onInit(m: Flow): void;
  onUpdate(m: Flow): void;
}
