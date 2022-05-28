import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {TextContent} from "../TextContent";

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent implements OnInit {
  confirmation: string;

  constructor(private dialogRef: MatDialogRef<ConfirmDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: string,
  ) { }

  ngOnInit(): void {
    this.confirmation = this.data;
  }

  onConfirm() {
    this.dialogRef.close(true);
  }

  onDiscard() {
    this.dialogRef.close(false);
  }
}
