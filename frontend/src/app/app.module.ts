import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MainFeedComponent} from './main-feed/main-feed.component';
import {TextCardComponent} from './text/text-card/text-card.component';
import {MatCardModule} from "@angular/material/card";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {MatButtonModule} from "@angular/material/button";
import {AccountComponent} from './auth/account/account.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatDividerModule} from "@angular/material/divider";
import {MatToolbarModule} from "@angular/material/toolbar";
import {ToolbarComponent} from './toolbar/toolbar.component';
import {MatMenuModule} from "@angular/material/menu";
import {MatDialogModule} from "@angular/material/dialog";
import {ShareDialogComponent} from './text/share-dialog/share-dialog.component';
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {PublicTextViewComponent} from './text/public-text-view/public-text-view.component';
import {MatBottomSheetModule} from "@angular/material/bottom-sheet";
import {ForgotPasswordComponent} from './auth/forgot-password/forgot-password.component';
import {JwtModule} from "@auth0/angular-jwt";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ToastrModule} from "ngx-toastr";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {EditDialogComponent} from './text/edit-dialog/edit-dialog.component';
import {ConfirmDialogComponent} from './text/confirm-dialog/confirm-dialog.component';
import {RouterModule, Routes} from "@angular/router";
import {RefreshTokenInterceptor} from "./auth/auth.service";

const routes: Routes = [
  {path: '', component: MainFeedComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    MainFeedComponent,
    TextCardComponent,
    AccountComponent,
    ToolbarComponent,
    ShareDialogComponent,
    PublicTextViewComponent,
    ForgotPasswordComponent,
    EditDialogComponent,
    ConfirmDialogComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatGridListModule,
    MatIconModule,
    MatButtonToggleModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatDividerModule,
    MatToolbarModule,
    MatMenuModule,
    MatDialogModule,
    MatSlideToggleModule,
    FormsModule,
    HttpClientModule,
    MatBottomSheetModule,
    RouterModule.forRoot(routes),
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    MatProgressSpinnerModule
  ],
  exports: [RouterModule],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: RefreshTokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
