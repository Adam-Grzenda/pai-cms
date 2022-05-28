import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AccountComponent} from "../auth/account/account.component";
import {AuthService} from "../auth/auth.service";
import {EditDialogComponent} from "../text/edit-dialog/edit-dialog.component";

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent implements OnInit {
  GUEST_NAME: string = "Guest";

  isSignedIn: boolean = false;
  username: string = this.GUEST_NAME;

  constructor(
    private dialog: MatDialog,
    private authService: AuthService
  ) {
  }

  ngOnInit(): void {
    let currentUser = this.authService.getCurrentUser();
    if(currentUser.loggedIn && currentUser.email) {
      this.isSignedIn = true;
      this.username = currentUser.email;
    }
  }

  signIn() {
    let dialogRef = this.dialog.open(AccountComponent, {
      minWidth: '50%',
      maxWidth: '60%'
    })

    dialogRef.afterClosed().subscribe(_ => {
      let currentUser = this.authService.getCurrentUser()
      if (currentUser.loggedIn && currentUser?.email) {
        this.isSignedIn = true;
        this.username = currentUser.email;
      } else {
        this.username = this.GUEST_NAME;
        this.isSignedIn = false;
      }
    })

  }

  signOut() {
    this.authService.logout()
    this.isSignedIn = false;
    this.username = this.GUEST_NAME;

  }

  addText() {
    let dialogRef = this.dialog.open(EditDialogComponent, {
      minWidth: '50%',
      maxWidth: '60%'
    })
  }
}
