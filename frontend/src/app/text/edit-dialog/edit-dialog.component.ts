import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TextContentService} from "../text.service";
import {TextContent} from "../TextContent";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ToastService} from "../../toast.service";

@Component({
  selector: 'app-edit-dialog',
  templateUrl: './edit-dialog.component.html',
  styleUrls: ['./edit-dialog.component.css']
})
export class EditDialogComponent implements OnInit {
  editForm: FormGroup = this.fb.group(
    {
      title: ['', [Validators.required]],
      subtitle: ['', [Validators.required]],
      content: ['', [Validators.required]],
    }
  )

  constructor(private fb: FormBuilder,
              private textContentService: TextContentService,
              private dialogRef: MatDialogRef<EditDialogComponent>,
              private toastService: ToastService,
              @Inject(MAT_DIALOG_DATA) public data: TextContent,
  ) {
  }

  ngOnInit(): void {
    if (this.data) {
      this.editForm.controls["title"].setValue(this.data.title)
      this.editForm.controls["subtitle"].setValue(this.data.subtitle)
      this.editForm.controls["content"].setValue(this.data.content)
    }
  }

  onSubmit() {
    const title: string = this.editForm.get("title")?.value
    const subtitle: string = this.editForm.get("subtitle")?.value
    const content: string = this.editForm.get("content")?.value

    let textContent: TextContent;

    if (!this.data) {
      textContent = new TextContent()
    } else {
      textContent = this.data
    }

    textContent.title = title;
    textContent.subtitle = subtitle;
    textContent.content = content;

    if (this.data) {
      this.textContentService.putText(textContent).subscribe(
        {
          next: _ => this.dialogRef.close(),
          error: _ => this.toastService.showUnexpectedError()
        }
      )
    } else {
      this.textContentService.postText(textContent).subscribe(
        {
          next: _ => this.dialogRef.close(),
          error: _ => this.toastService.showUnexpectedError()
        }
      )
    }


  }
}
