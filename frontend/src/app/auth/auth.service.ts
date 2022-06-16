import {EventEmitter, Injectable} from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpParams,
  HttpRequest
} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {catchError, filter, map, switchMap, take} from "rxjs/operators"
import {BehaviorSubject, Observable, of, throwError} from "rxjs";
import {JwtHelperService} from "@auth0/angular-jwt";
import {Router} from "@angular/router";

const ACCESS_TOKEN = "access_token";
const REFRESH_TOKEN = "refresh_token";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private static readonly EMAIL_PARAMETER = "email";
  private static readonly PASSWORD_PARAMETER = "password";

  private jwtHelper: JwtHelperService = new JwtHelperService();

  authEvent: EventEmitter<AuthEventType> = new EventEmitter<AuthEventType>();

  private headers = new HttpHeaders(
    {
      'Content-Type': 'application/x-www-form-urlencoded',
      Accept: '*/*',
    }
  );

  constructor(private http: HttpClient) {
  }

  login(email: string, password: string): Observable<AuthServiceResponse> {
    const body = AuthService.createAuthenticationBody(email, password)

    return this.http.post<AuthTokens>(`${environment.apiHost}/login`, body.toString(), {headers: this.headers}).pipe(
      map((value: AuthTokens) => {
          localStorage.setItem(ACCESS_TOKEN, value.access_token);
          localStorage.setItem(REFRESH_TOKEN, value.refresh_token);
          this.authEvent.emit(AuthEventType.LOGIN);
          return new AuthServiceResponse(true, this.jwtHelper.decodeToken(value.access_token).sub)
        }
      ),
      catchError(_ => of(new AuthServiceResponse(false)))
    )
  }

  refreshToken(): Observable<AuthTokens> {
    const body = new HttpParams().append("refreshToken", "Bearer " + localStorage.getItem(REFRESH_TOKEN) ?? "");
    return this.http.post<AuthTokens>(`${environment.apiHost}/refresh`, body)
  }

  getAccessToken(): string | null {
    return localStorage.getItem(ACCESS_TOKEN)
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
    this.authEvent.emit(AuthEventType.LOGOUT)
  }

  newPassword(email: string, token: string, password: string): Observable<any> {
    return this.http.post<any>(environment.apiHost + "/reset-password", {
      token: token,
      username: email,
      password: password
    })
  }

  register(email: string, password: string): Observable<boolean> {
    const body = AuthService.createAuthenticationBody(email, password)

    return this.http.post<any>(`${environment.apiHost}/register`, body.toString(), {headers: this.headers}).pipe(
      map((value: AuthTokens) => {
          return true
        }
      )
    )
  }

  forgottenPassword(email: string): Observable<boolean> {
    const forgottenPasswordBody = new HttpParams().append(AuthService.EMAIL_PARAMETER, email);

    return this.http.post<any>(`${environment.apiHost}/forgot-password`, forgottenPasswordBody.toString(), {headers: this.headers}).pipe(
      map((_) => {
          return true
        }
      )
    )

  }

  private static createAuthenticationBody(email: string, password: string) {
    return new HttpParams().append(this.EMAIL_PARAMETER, email).append(this.PASSWORD_PARAMETER, password);
  }

  checkUserExisting(email: string): Observable<boolean> {
    const nameParams = new HttpParams().append(AuthService.EMAIL_PARAMETER, email);
    return this.http.post<boolean>(environment.apiHost + "/user", nameParams)
  }
}

@Injectable()
export class RefreshTokenInterceptor implements HttpInterceptor {

  private isRefreshing = false
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null)

  constructor(private authService: AuthService,
              private router: Router) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    let currentToken = this.authService.getAccessToken()

    if (currentToken != null) {
      req = this.addToken(req, currentToken)
    }


    return next.handle(req).pipe(
      catchError((error, _) => {
        if (error instanceof HttpErrorResponse && error.status == 401) {
          return this.handleUnauthorized(req, next)
        } else {
          return throwError(error);
        }
      })
    )
  }


  private handleUnauthorized(request: HttpRequest<any>, next: HttpHandler) {

    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.authService.refreshToken().pipe(
        switchMap((tokens: AuthTokens) => {
          this.isRefreshing = false
          this.refreshTokenSubject.next(tokens.access_token)
          localStorage.setItem(REFRESH_TOKEN, tokens.refresh_token)
          localStorage.setItem(ACCESS_TOKEN, tokens.access_token)
          return next.handle(this.addToken(request, tokens.access_token))
        }),
        catchError(error => {
          return of()
        })
      )
    } else {
      return this.refreshTokenSubject.pipe(
        filter(token => token != null),
        take(1),
        switchMap(token => {
          return next.handle(this.addToken(request, token))
        })
      )
    }


  }


  private addToken(request: HttpRequest<any>, access_token: string) {
    return request.clone(
      {
        setHeaders: {
          'Authorization': 'Bearer ' + access_token
        }
      }
    );
  }
}


enum AuthEventType {
  LOGIN,
  LOGOUT
}


class AuthTokens {
  access_token: string;
  refresh_token: string;
}


export class AuthServiceResponse {

  constructor(public loggedIn: boolean, public email: string | null = null) {
  }
}
