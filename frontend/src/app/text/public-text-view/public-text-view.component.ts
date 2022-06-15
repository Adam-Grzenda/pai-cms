import {Component, OnInit} from '@angular/core';
import {TextContent} from "../TextContent";
import {TextContentService} from "../text.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-public-text-view',
  templateUrl: './public-text-view.component.html',
  styleUrls: ['./public-text-view.component.css']
})
export class PublicTextViewComponent implements OnInit {

  textContent: TextContent | null = null

  constructor(private textContentService: TextContentService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    let textContentId = this.route.snapshot.params['text-content-id'];
    let token = this.route.snapshot.queryParamMap.get("token")

    console.log(token, textContentId)
    if (token) {
      this.textContentService.getPublicText(textContentId, token).subscribe(
        result => this.textContent = result
      )
    }
  }


}
