import {Component, Input, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {ShareDialogComponent} from "../share-dialog/share-dialog.component";

@Component({
  selector: 'app-text-card',
  templateUrl: './text-card.component.html',
  styleUrls: ['./text-card.component.css']
})
export class TextCardComponent implements OnInit {
  @Input()
  id: number;

  @Input()
  public title: String;

  @Input()
  public subtitle: String;

  @Input()
  public text: String;

  public isShared: boolean = true;

  @Input()
  public isOwner: boolean = true;

  constructor(
    private dialog: MatDialog
  ) {
  }

  ngOnInit(): void {
  }

  onClickShare(): void {
    this.dialog.open(ShareDialogComponent, {
      data: {isShared: this.isShared},
      minWidth: "30%",
      minHeight: "30%"
    })
  }
}
