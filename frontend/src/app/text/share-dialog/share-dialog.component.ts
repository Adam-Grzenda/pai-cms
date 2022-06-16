import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import {TextContent} from "../TextContent";
import {TextContentService} from "../text.service";
import {Clipboard} from '@angular/cdk/clipboard';
import {ToastService} from "../../toast.service";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";

@Component({
  selector: 'app-share-dialog',
  templateUrl: './share-dialog.component.html',
  styleUrls: ['./share-dialog.component.css']
})
export class ShareDialogComponent implements OnInit {
  publicLink: string;
  shareToggled: boolean;
  textContent: TextContent;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: TextContent,
    private dialog: MatDialog,
    private textContentService: TextContentService,
    private clipboard: Clipboard,
    private toastService: ToastService
  ) {
  }

  ngOnInit(): void {
    this.shareToggled = this.data.isShared
    if (this.shareToggled) {
      this.textContentService.setShared(this.data, true).subscribe(
        token => {
          this.publicLink = this.createPublicLink(this.data, token)
        }
      )
    }
  }

  private createPublicLink(content: TextContent, token: string) {
    return "localhost:4200/public/" + content.id + "/?token=" + token;
  }


  onToggleChange() {
    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
        minWidth: "30%",
        minHeight: "30%",
        data: "Do your really want to change your content access policy?"
      }
    )

    dialogRef.afterClosed().subscribe(
      result => {
        if (result) {
          this.toggleShared()
        }
      }
    )

  }

  private toggleShared() {
    if (this.shareToggled) {
      this.textContentService.setShared(this.data, true).subscribe(
        token => {
          this.publicLink = this.createPublicLink(this.data, token)
          this.textContentService.textContentEvent.emit()
        }
      )
    } else if (!this.shareToggled) {
      this.textContentService.setShared(this.data, false).subscribe(
        _ => {
          this.publicLink = ""
          this.textContentService.textContentEvent.emit()
        }
      )
    }
  }

  copyLinkToClipboard() {
    this.clipboard.copy(this.publicLink);
    this.toastService.showSuccess("Copied link to clipboard")
  }
}
