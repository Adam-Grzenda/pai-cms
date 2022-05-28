import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, ValidationErrors, Validators} from "@angular/forms";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {ForgotPasswordComponent} from "../forgot-password/forgot-password.component";
import {AuthService, AuthServiceResponse} from "../auth.service";
import {MatDialogRef} from "@angular/material/dialog";
import {ToastService} from "../../toast.service";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  login: boolean = true;

  registrationForm = this.fb.group(
    {
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeated: ['', [Validators.required, this.passwordMatching]]
    }
  );

  constructor(private fb: FormBuilder,
              private bottomSheet: MatBottomSheet,
              private authService: AuthService,
              private dialogRef: MatDialogRef<AccountComponent>,
              private toastService: ToastService) {
  }

  ngOnInit(): void {
  }

  onSubmit() {
    const email: string = this.registrationForm.get("email")?.value
    const password: string = this.registrationForm.get("password")?.value

    if (this.login) {
      this.authService.login(email, password).subscribe(
        {
          next: (authServiceResponse: AuthServiceResponse) => {
            if (authServiceResponse.loggedIn) {
              this.dialogRef.close();
            } else {
              this.registrationForm.get("password")?.reset();
            }
          },
          error: (err => console.log(err))
        }
      )
    } else {
      this.authService.register(email, password).subscribe(
        {
          next: _ => {
            this.login = true;
            this.toastService.showSuccess("User registered successfully");
          },
          error: _ => this.toastService.showUnexpectedError()
        }
      )
    }


  }

  passwordMatching(control: AbstractControl): ValidationErrors | null {
    const password = control.get("password")?.value;
    const passwordRepeated = control.get("passwordRepeated")?.value;


    if (password != passwordRepeated) {
      return {'noMatch': true};
    } else
      return null;
  }

  forgotPassword() {
    this.bottomSheet.open(ForgotPasswordComponent)
  }
}