export class SecretDtos {
}

export enum SMethod {
  encrypt = 'encrypt', decrypt = 'decrypt'
}

export class InPlain {
  public plain: string;
  public name: string;
  public passHash: string;
}

export class SecretResult {
  public method: SMethod;
  public data: string;
}
