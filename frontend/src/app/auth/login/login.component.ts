import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, ValidationErrors, Validators} from "@angular/forms";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {ForgotPasswordComponent} from "../forgot-password/forgot-password.component";
import {AuthService, AuthServiceResponse, UserDetails} from "../auth.service";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

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
              private dialogRef: MatDialogRef<LoginComponent>) {
  }

  ngOnInit(): void {
  }

  onSubmit() {
    const email: string = this.registrationForm.get("email")?.value
    const password: string = this.registrationForm.get("password")?.value

    console.log(email + " " + password);
    if (this.login) {
      this.authService.login(email, password).subscribe(
        {
          next: (authServiceResponse: AuthServiceResponse) => {
            if (authServiceResponse.successful) {
              this.dialogRef.close();
            } else {
              this.registrationForm.get("password")?.reset()
            }
          },
          error: (err => console.log(err))
        }
      )
    }


    console.warn("not implemented: " + this.registrationForm.value)
    console.log(this.registrationForm.get("password"))
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
