import {Component, OnInit} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {TextContentService} from "../text/text.service";
import {TextContent} from "../text/TextContent";
import {SpinnerService} from "../spinner.service";
import {ToastService} from "../toast.service";

@Component({
  selector: 'app-main-feed',
  templateUrl: './main-feed.component.html',
  styleUrls: ['./main-feed.component.css']
})
export class MainFeedComponent implements OnInit {

  constructor(private authService: AuthService, private textContentService: TextContentService, private spinnerService: SpinnerService, private toastService: ToastService) {
  }

  loadedContent: TextContent[];

  title: String = "Some random title";
  subtitle: String = "Some random subtitle";
  content: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque eu dapibus dui. Proin mattis condimentum ex, eget condimentum massa placerat eu. Mauris justo erat, bibendum at lacus non, facilisis feugiat orci. Morbi sit amet urna orci. Integer varius quam quis nisl posuere, nec semper libero molestie. Ut ac lectus vitae ante scelerisque consectetur. Maecenas non fringilla ligula. Vivamus blandit interdum nulla eu scelerisque. Etiam finibus, lorem vitae varius convallis, diam purus egestas metus, quis elementum justo sapien quis lorem. Integer at pulvinar est, sit amet iaculis lorem. Donec at orci sagittis, sodales justo ut, imperdiet tellus. Etiam suscipit sagittis consectetur.Aliquam pellentesque sem cursus turpis placerat tristique. Suspendisse ut ullamcorper velit. Nullam finibus vitae tellus quis consequat. Aenean tellus velit, vestibulum et hendrerit consectetur, semper ut mi. Sed luctus ultricies tellus id eleifend. Nullam finibus neque finibus urna facilisis, sit amet sodales tellus varius. In laoreet purus eget placerat vulputate.Suspendisse convallis rutrum lorem. Nam nunc massa, mattis ac pulvinar nec, auctor vel ex. Suspendisse sed elit quis tortor ullamcorper ornare. Praesent ac vestibulum tortor, nec bibendum quam. Nulla tincidunt nisl mi, at efficitur dolor posuere ut. Integer laoreet mauris massa, id rutrum nulla mollis in. Quisque varius vehicula purus, sed imperdiet dui pharetra eget. Sed ac tortor pretium, tristique dolor ut, tincidunt nisi. Praesent euismod non nulla eget elementum. Nulla aliquet est quis ex consectetur ullamcorper. Aliquam arcu velit, eleifend in arcu eu, aliquam hendrerit elit. Quisque varius commodo egestas.";

  title2: String = "Second random title";
  subtitle2: String = "Second random subtitle";
  content2: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque eu dapibus dui. Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

  ngOnInit(): void {
    this.loadTexts()
  }

  loadTexts() {
    if (this.authService.getCurrentUser().loggedIn) {
      this.textContentService.getTexts().subscribe(
        {
          next: (value => this.loadedContent = value.content),
          error: (error => {
            this.toastService.showError("Failed to load content");
            this.loadedContent = Array<TextContent>()
          })
        }
      )
    }
  }


}
