import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {GuestService} from "../../../../../core/services/guest.service";
import {DataService} from "../../../../../core/services/data.service";

@Component({
  selector: 'app-guest-entry-form',
  templateUrl: './guest-entry-form.component.html',
  styleUrls: ['./guest-entry-form.component.css']
})
export class GuestEntryFormComponent implements OnInit {
  mode: String;
  guestEntryForm: FormGroup;
  guestEntryImageForm: FormGroup;
  guestEntry: any = {};
  guestEntryLocal: any = {};
  selectedFile: File= null;
  context: any;
  constructor(private  guestService: GuestService, private dataService: DataService) { }

  ngOnInit(): void {
    this.dataService.modelDataObject.subscribe(modelObject => {
      if (modelObject.data != null) {
        this.createGuestEntryForm(modelObject.data);
        this.createGuestEntryImageForm(modelObject.data);
        this.mode = modelObject.mode;
        this.guestEntryLocal = modelObject.data;
        this.context = modelObject.context;
      }
    });
    this.createGuestEntryForm(this.guestEntry);
    this.createGuestEntryImageForm(this.guestEntry);
  }

  private createGuestEntryForm(data:any) {
    // binding data with form
    // let formData = null;
    // if (data != null) {
    //   formData = data;
    // }
    this.guestEntryForm = new FormGroup({
      guestBookEntryText: new FormControl(data.guestBookEntryText, Validators.required),
      guestBookEntryId: new FormControl(data.guestBookEntryId, Validators.required),
    });
  }
  private createGuestEntryImageForm(data:any) {
    // let formData = {};
    // if (data != null) {
    //   formData = data;
    // }
    this.guestEntryImageForm = new FormGroup({
      file: new FormControl(null, Validators.required),
      guestBookEntryId: new FormControl(data.guestBookEntryId, Validators.required),
    });
  }

  onGuestEntryUpdate() {
    if (this.mode == 'edit') {
      this.guestService.updateGuestEntryText(this.guestEntryForm.value).subscribe(response => {
        // Refreshing the list, calling method from the context
        this.dataService.context.getGuestEntryList();
        this.dataService.buildModelDataObject(null, 'close', this.context, response['infoMessages'], null);
      }, error => {
        this.dataService.buildModelDataObject(null, null, this.context, null, error.error.errorMessages);
      });
   }
  }

  onFileChanged($event: Event) {
    console.log($event.target['files'][0])
    this.selectedFile = $event.target['files'][0];
    if (this.selectedFile.size >= 3048576) {
      alert('File size cannot be greater than 3MB');
      this.selectedFile = null;
    }
  }

  clearImageSelection() {
    this.selectedFile = null;
  }

  onGuestEntryImageFormUpdate() {
    if (this.mode == 'edit') {
      const uploadImageData = new FormData();
      uploadImageData.append('file', this.selectedFile, this.selectedFile.name);
      uploadImageData.append("guestBookEntryId", this.guestEntryImageForm.value.guestBookEntryId);
      this.guestService.updateGuestEntryImage(uploadImageData).subscribe(response => {
        // Refreshing the list, calling method from the context
        this.dataService.context.getGuestEntryList(true);
        this.dataService.buildModelDataObject(null, null, this.context, response['infoMessages'], null);
      }, error => {
        this.dataService.buildModelDataObject(null, null, this.context, null, error.error.errorMessages);
      });
    }
  }
}
