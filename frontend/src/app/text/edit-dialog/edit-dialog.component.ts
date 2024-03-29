import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TextContentService} from "../text.service";
import {TextContent} from "../TextContent";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ToastService} from "../../toast.service";
import {MatChip, MatChipList} from "@angular/material/chips";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";

@Component({
  selector: 'app-edit-dialog',
  templateUrl: './edit-dialog.component.html',
  styleUrls: ['./edit-dialog.component.css']
})
export class EditDialogComponent implements OnInit {

  availableTags: string[];
  selectedTags: string[] = new Array<string>();

  @ViewChild("chipList")
  chipList: MatChipList

  editForm: FormGroup = this.fb.group(
    {
      title: ['', [Validators.required]],
      subtitle: ['', [Validators.required]],
      content: ['', [Validators.required]],
      imageSource: []
    }
  )

  constructor(private fb: FormBuilder,
              private dialog: MatDialog,
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
      this.editForm.controls["imageSource"].setValue(this.data.imageHref)
      this.selectedTags = this.data.tags
    }

    this.textContentService.getAvailableTags().subscribe(
      result => {
        this.availableTags = result
      }
    );
  }


  onSubmit() {

    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
        minWidth: "30%",
        minHeight: "30%",
        data: "Do your really want to submit your changes?"
      }
    )

    dialogRef.afterClosed().subscribe(
      result => {
        if (result) {
          this.submit()
        }
      }
    )


  }

  private submit() {
    const title: string = this.editForm.get("title")?.value
    const subtitle: string = this.editForm.get("subtitle")?.value
    const content: string = this.editForm.get("content")?.value
    const imageSource: string = this.editForm.get("imageSource")?.value

    let textContent: TextContent;

    if (!this.data) {
      textContent = new TextContent()
    } else {
      textContent = this.data
    }

    textContent.title = title;
    textContent.subtitle = subtitle;
    textContent.content = content;
    textContent.tags = this.selectedTags
    textContent.imageHref = imageSource


    if (this.data) {
      this.textContentService.putText(textContent).subscribe(
        {
          next: _ => {
            this.dialogRef.close()
            this.textContentService.textContentEvent.emit()
          },
          error: error => {
            this.toastService.showError(error.error.message)
            this.textContentService.textContentEvent.emit()
          }
        }
      )
    } else {
      this.textContentService.postText(textContent).subscribe(
        {
          next: _ => {
            this.dialogRef.close()
            this.textContentService.textContentEvent.emit()
          },
          error: error => {
            this.toastService.showError(error.error.message)
            this.textContentService.textContentEvent.emit()
          }
        }
      )
    }
  }

  onClickChip(chip: MatChip, chipList: MatChipList) {
    chip.toggleSelected()
    let selectedChips = chipList.selected;

    if (selectedChips instanceof MatChip) {
      this.selectedTags = [selectedChips.value]
    } else {
      this.selectedTags = selectedChips.map(chip => chip.value)
    }

  }


}
