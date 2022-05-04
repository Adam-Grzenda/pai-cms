import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-text-card',
  templateUrl: './text-card.component.html',
  styleUrls: ['./text-card.component.css']
})
export class TextCardComponent implements OnInit {
  @Input()
  public title: String;

  @Input()
  public subtitle: String;

  @Input()
  public text: String

  constructor(
  ) {
  }

  ngOnInit(): void {
  }

}
