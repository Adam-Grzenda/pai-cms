import {Component, OnInit, ViewChild} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {TextContentService} from "../text/text.service";
import {TextContent} from "../text/TextContent";
import {ToastService} from "../toast.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatChip, MatChipList} from "@angular/material/chips";

@Component({
  selector: 'app-main-feed',
  templateUrl: './main-feed.component.html',
  styleUrls: ['./main-feed.component.css']
})
export class MainFeedComponent implements OnInit {

  constructor(private authService: AuthService,
              private textContentService: TextContentService,
              private toastService: ToastService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  loadedContent: TextContent[];
  initialTags: string[] = new Array<string>()

  loading: boolean = false;
  availableTags: string[];
  selectedTags: string[];

  @ViewChild("chipList")
  chipList: MatChipList

  ngOnInit(): void {
    this.loadTexts()
    this.textContentService.getAvailableTags().subscribe(
      result => this.availableTags = result
    )

    this.authService.authEvent.subscribe(
      _ => {
        this.loadTexts()
        this.router.navigate([""])
      }
    )

    this.textContentService.textContentEvent.subscribe(_ => {
      this.loadTexts()
      this.router.navigate([""])
    })


    let tagsParam = this.route.snapshot.queryParamMap.get("tags")?.split(",")
    if (tagsParam) {
      this.initialTags = tagsParam
    }

    this.route.queryParamMap.subscribe(queryParams => {
      let searchKeyword = queryParams.get("search") ?? undefined;
      let tagsFilter = queryParams.get("tags") ?? undefined
      this.loadTexts(searchKeyword, tagsFilter)
    })

  }


  loadTexts(searchKeyword: string | undefined = undefined, tagsFilter: string | undefined = undefined) {
    this.loading = true;
    if (this.authService.getCurrentUser().loggedIn) {
      if (searchKeyword || tagsFilter) {
        this.loadFiltered(searchKeyword??"", tagsFilter??"");
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

  private loadFiltered(searchKeyword: string, tagsFilter: string) {
    this.textContentService.getTextsFiltered(searchKeyword, tagsFilter).subscribe(
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

  onClickChip(chip: MatChip, chipList: MatChipList) {
    chip.toggleSelected()
    let selectedChips = chipList.selected;

    if (selectedChips instanceof MatChip) {
      this.selectedTags = [selectedChips.value]
    } else {
      this.selectedTags = selectedChips.map(chip => chip.value)
    }

    this.router.navigate([], {
      queryParams: {
        tags: this.selectedTags.map(item => item.trim()).join(",")
      },
      queryParamsHandling: "merge"
    });
  }
}
