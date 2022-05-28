import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {ShareDialogComponent} from "../share-dialog/share-dialog.component";
import {TextContentService} from "../text.service";
import {EditDialogComponent} from "../edit-dialog/edit-dialog.component";
import {TextContent} from "../TextContent";
import {ToastService} from "../../toast.service";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";

@Component({
  selector: 'app-text-card',
  templateUrl: './text-card.component.html',
  styleUrls: ['./text-card.component.css']
})
export class TextCardComponent implements OnInit {

  public isShared: boolean = true;

  @Input()
  isOwner: boolean;

  @Input()
  textContent: TextContent;

  @Output()
  textChanged: EventEmitter<any> = new EventEmitter<any>();

  constructor(
    private dialog: MatDialog,
    private textContentService: TextContentService,
    private toastService: ToastService
  ) {
  }

  ngOnInit(): void {
  }

  onClickShare(): void {
    this.dialog.open(ShareDialogComponent, {
      data: this.textContent,
      minWidth: "30%",
      minHeight: "30%"
    })
  }

  onClickEdit() {
    this.dialog.open(EditDialogComponent, {
      data: this.textContent,
      minWidth: "50%",
      minHeight: "60%"
    })
  }

  onClickDelete() {

    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
      minWidth: "30%",
      minHeight: "30%"
    })

    dialogRef.afterClosed().subscribe(
      result => {
        if (result) {
          this.textContentService.deleteText(this.textContent).subscribe(
            {
              next: value => {
                this.toastService.showSuccess("Content deleted successfully")
                this.textChanged.emit();
              },
              error: err => this.toastService.showUnexpectedError()
            }
          )
        }
      }
    )
  }
}
