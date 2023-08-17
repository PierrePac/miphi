import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: 'formatEnum'
})
export class FormatEnumPipe implements PipeTransform {
  transform(value: string): string {
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }
}
