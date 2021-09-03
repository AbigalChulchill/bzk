import { Type } from "class-transformer";

export class PageDto<T> {

  public content = new Array<T>();
  @Type(() => Pageable)
  public pageable: Pageable;
  public totalPages: number;
  public totalElements: number;
  public last: number;
  public size: number;
  @Type(() => Sort)
  public sort: Sort;
  public numberOfElements: number;
  public first: boolean;
  public empty: boolean;
}

export class Pageable {
  public offset: number;
  public pageSize: number;
  public pageNumber: number;
  public paged: boolean;
  public unpaged: boolean;
}

export class Sort {
  public sorted: boolean;
  public unsorted: boolean;
  public empty: boolean;
}
