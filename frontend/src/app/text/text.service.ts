import {Injectable} from '@angular/core';
import {TextContent, TextContentPage} from "./TextContent";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class TextContentService {

  constructor(private http: HttpClient) {
  }

  postText(textContent: TextContent): Observable<TextContent> {
    return this.http.post <TextContent>(environment.contentApiBaseUrl + "/texts", textContent);
  }

  putText(textContent: TextContent): Observable<TextContent> {
    return this.http.put <TextContent>(environment.contentApiBaseUrl + "/texts", textContent);
  }

  getTexts(): Observable<TextContentPage> {
    return this.http.get<TextContentPage>(environment.contentApiBaseUrl + "/texts", {observe: "body"})
  }

}
