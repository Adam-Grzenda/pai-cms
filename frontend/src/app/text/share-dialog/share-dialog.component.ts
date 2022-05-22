import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
  selector: 'app-share-dialog',
  templateUrl: './share-dialog.component.html',
  styleUrls: ['./share-dialog.component.css']
})
export class ShareDialogComponent implements OnInit {
  publicLink: String;

  shareToggled: boolean;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: {isShared: boolean}
  ) {
    this.shareToggled = !this.data.isShared
  }

  ngOnInit(): void {
    if (this.shareToggled) {
      this.publicLink = "localhost:8080/test"
    }
  }


  onToggleChange() {
    console.log(this.shareToggled)
    if (!this.shareToggled) {
      this.publicLink = "localhost:8080/test";
    } else if (this.shareToggled){
      this.publicLink = "";
    }
  }
}
