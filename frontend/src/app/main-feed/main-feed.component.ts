import {Component, OnInit} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {TextContentService} from "../text/text.service";
import {TextContent} from "../text/TextContent";
import {ToastService} from "../toast.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-main-feed',
  templateUrl: './main-feed.component.html',
  styleUrls: ['./main-feed.component.css']
})
export class MainFeedComponent implements OnInit {

  constructor(private authService: AuthService,
              private textContentService: TextContentService,
              private toastService: ToastService,
              private route: ActivatedRoute) {
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

    this.route.queryParamMap.subscribe(queryParams => {
      let searchKeyword = queryParams.get("search");
      if (searchKeyword) {
        this.loadTexts(searchKeyword)
      } else {
        this.loadTexts()
      }
    })

  }


  loadTexts(searchKeyword: string = "") {
    this.loading = true;
    if (this.authService.getCurrentUser().loggedIn) {
      if (searchKeyword != "") {
        this.loadFiltered(searchKeyword);
      } else {
        this.loadUnfiltered();
      }
    } else {
      this.loadedContent = Array<TextContent>();
      console.error("USER NOT LOGGED IN - NO CONTENT")
    }
  }

  private loadUnfiltered() {
    this.textContentService.getTexts().subscribe(
      {
        next: (value => {
          this.loadedContent = value.content
          this.loading = false
        }),
        error: (error => {
          this.toastService.showError("Failed to load content");
          this.loadedContent = Array<TextContent>();
          this.loading = false;
        })
      }
    )
  }

  private loadFiltered(searchKeyword: string) {
    this.textContentService.getTextsFiltered(searchKeyword).subscribe(
      {
        next: (value => {
          this.loadedContent = value
          this.loading = false
        }),
        error: (error => {
          this.toastService.showError("Failed to load content");
          this.loadedContent = Array<TextContent>();
          this.loading = false;
        })
      }
    )
  }
}
