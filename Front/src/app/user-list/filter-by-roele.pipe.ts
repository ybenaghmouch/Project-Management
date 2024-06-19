import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'filterByRole',
    standalone: true // Make the pipe standalone
  })
  export class FilterByStatutPipe implements PipeTransform {
    transform(items: any[], statut: string): any[] {
      if (!items || !statut) {
        return items;
      }
      return items.filter(item => item.statut === statut);
    }
  }
