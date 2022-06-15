import {Injectable} from '@angular/core';
import {TextContent} from "./TextContent";
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

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

  getTexts(): Observable<TextContent[]> {
    return this.http.get<TextContent[]>(environment.contentApiBaseUrl + "/texts", {observe: "body"})
  }

  deleteText(textContent: TextContent): Observable<any> {
    return this.http.delete<any>(environment.contentApiBaseUrl + "/texts/" + textContent.id);
  }

  getTextsFiltered(searchKeyword: string, tagsFilter: string): Observable<Array<TextContent>> {
    let params = new HttpParams()
    if (searchKeyword != "") {
      params = params.append("keyword", searchKeyword)
    }

    if (tagsFilter != "") {
      params = params.append("tags", tagsFilter)
    }

    return this.http.get<Array<TextContent>>(environment.contentApiBaseUrl + "/texts/search", {params: params})
  }

  getAvailableTags(): Observable<Array<string>> {
    return this.http.get<Array<string>>(environment.contentApiBaseUrl + "/texts/tags");
  }
}
