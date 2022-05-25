import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {catchError, map} from "rxjs/operators"
import {Observable, of} from "rxjs";
import {JwtHelperService} from "@auth0/angular-jwt";

const ACCESS_TOKEN = "access_token";
const REFRESH_TOKEN = "refresh_token";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private static readonly EMAIL_PARAMETER = "email";
  private static readonly PASSWORD_PARAMETER = "password";

  private headers = new HttpHeaders(
    {
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: '*/*',
    }
  );

  constructor(private http: HttpClient, private jwtHelper: JwtHelperService) {
  }

  login(email: string, password: string): Observable<AuthServiceResponse> {
    const body = AuthService.createAuthenticationBody(email, password)

    return this.http.post<AuthTokens>(environment.apiUrl + "/login", body.toString(), {headers: this.headers}).pipe(
      map((value: AuthTokens) => {
          localStorage.setItem(ACCESS_TOKEN, value.access_token);
          localStorage.setItem(REFRESH_TOKEN, value.refresh_token);
          return new AuthServiceResponse(true, this.jwtHelper.decodeToken(value.access_token).sub)
        }
      ),
      catchError(_ => of(new AuthServiceResponse(false)))
    )
  }

  getCurrentUser(): AuthServiceResponse {
    const access_token = localStorage.getItem(ACCESS_TOKEN);
    if (access_token) {
      return new AuthServiceResponse(true, this.jwtHelper.decodeToken(access_token).sub)
    } else {
      return new AuthServiceResponse(false, null);
    }
  }

  logout() {
    localStorage.removeItem(ACCESS_TOKEN)
    localStorage.removeItem(REFRESH_TOKEN)
  }


  register(email: string, password: string): Observable<boolean> {
    const body = AuthService.createAuthenticationBody(email, password)

    return this.http.post<any>(environment.apiUrl + "/register", body.toString(), {headers: this.headers}).pipe(
      map((value: AuthTokens) => {
          return true
        }
      ),
      catchError(_ => of(false))
    )
  }

  forgottenPassword(email: string): Observable<boolean> {
    const forgottenPasswordBody = new HttpParams().append(AuthService.EMAIL_PARAMETER, email);

    return this.http.post<any>(environment.apiUrl + "/forgot-password", forgottenPasswordBody.toString(), {headers: this.headers}).pipe(
      map((_) => {
          return true
        }
      )
    )

  }


  private static createAuthenticationBody(email: string, password: string) {
    return new HttpParams().append(this.EMAIL_PARAMETER, email).append(this.PASSWORD_PARAMETER, password);
  }


}


class AuthTokens {
  access_token: string;
  refresh_token: string;
}


export class AuthServiceResponse {

  constructor(public loggedIn: boolean, public email: string | null = null) {
  }
}
