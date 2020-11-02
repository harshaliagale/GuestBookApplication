import { Pipe, PipeTransform } from "@angular/core";


@Pipe({ name: "entryText" })
export class EntryTextPipe implements PipeTransform {

  transform(value: any) {
    if (!value) {
      return "Entry text not available"
    } else {
      let displayString = value.slice(0, 100);
      if (value.length > 99) {
        displayString = displayString + ' ...';
      }
      return displayString;
    }
  }
}
