export enum Authority {
  Admin = 'Admin', Payer = 'Payer', EndUser = 'EndUser'
}

export enum AuthProvider {
  local = 'local', facebook = 'facebook', google = 'google', github = 'github'
}

export class Account {

  public uid: string;
  public username: string;
  public email: string;
  public accountNonExpired: boolean;
  public accountNonLocked: boolean;
  public credentialsNonExpired: boolean;
  public enabled: boolean;
  public refCode: string;

  public authorities: Authority[] = [];

  public provider = AuthProvider.local;
  public providerId: string;

}
