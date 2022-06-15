import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AccountComponent} from "../auth/account/account.component";
import {AuthService} from "../auth/auth.service";
import {EditDialogComponent} from "../text/edit-dialog/edit-dialog.component";
import {ActivatedRoute, Router} from "@angular/router";

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
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    let currentUser = this.authService.getCurrentUser();
    if (currentUser.loggedIn && currentUser.email) {
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

  onEnter(event: Event) {
    let searchKeywords = (event.target as HTMLInputElement).value
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        search: searchKeywords
      },
      queryParamsHandling: "merge",
      skipLocationChange: false
    })
  }

  tryRefresh() {
    this.authService.refreshToken().subscribe( result=>
      console.log(result)
    )
  }
}
