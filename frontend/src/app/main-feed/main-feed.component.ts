import {Component, OnInit} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {TextContentService} from "../text/text.service";
import {TextContent} from "../text/TextContent";
import {ToastService} from "../toast.service";

@Component({
  selector: 'app-main-feed',
  templateUrl: './main-feed.component.html',
  styleUrls: ['./main-feed.component.css']
})
export class MainFeedComponent implements OnInit {

  constructor(private authService: AuthService, private textContentService: TextContentService, private toastService: ToastService) {
  }

  loadedContent: TextContent[];

  loading: boolean = false;

  ngOnInit(): void {
    this.loadTexts()

    this.authService.authEvent.subscribe(
      _ => {
       this.loadTexts()
      }
    )
  }

  loadTexts() {
    this.loading = true;

    if (this.authService.getCurrentUser().loggedIn) {
      this.textContentService.getTexts().subscribe(
        {
          next: (value => {
            this.loadedContent = value.content
            setTimeout(() => {this.loading = false;}, 3000) //todo just for some tests
          }),
          error: (error => {
            this.toastService.showError("Failed to load content");
            this.loadedContent = Array<TextContent>();
            this.loading = false;
          })
        }
      )
    } else {
      this.loadedContent = Array<TextContent>();
      console.error("USER NOT LOGGED IN - NO CONTENT")
    }
  }

}
