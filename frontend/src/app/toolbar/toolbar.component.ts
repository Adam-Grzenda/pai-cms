import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {LoginComponent} from "../login/login.component";

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent implements OnInit {
  GUEST_NAME: String = "Guest";

  isSignedIn: boolean = false;
  username: String = this.GUEST_NAME;

  constructor(
    private dialog: MatDialog
  ) {
  }

  ngOnInit(): void {
  }

  signIn() {
    this.isSignedIn = true;
    this.dialog.open(LoginComponent, {
      minWidth: '50%',
      maxWidth: '60%'
    })
    this.username = "Adam"
  }

  signOut() {
    this.isSignedIn = false;
    this.username = this.GUEST_NAME;
  }
}
