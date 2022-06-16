import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, ValidationErrors, Validators} from "@angular/forms";
import {AuthService} from "../../auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastService} from "../../../toast.service";

@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrls: ['./new-password.component.css']
})
export class NewPasswordComponent implements OnInit {

  newPasswordForm = this.fb.group(
    {
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeated: ['', [Validators.required, this.passwordMatching]]
    }
  );

  private token: string | null;
  private email: string | null;


  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private route: ActivatedRoute,
              private toastService: ToastService,
              private router: Router) {
  }

  ngOnInit(): void {
    let params = this.route.snapshot.queryParamMap;
    this.token = params.get("token")
    this.email = params.get("email")
  }

  onSubmit() {
    let password = this.newPasswordForm.get("password")?.value;
    let repeatedPassword = this.newPasswordForm.get("passwordRepeated")?.value;

    if (password != repeatedPassword) {
      this.newPasswordForm.get("passwordRepeated")?.setErrors(["Password does not match"])
      return
    } else if (this.email && this.token && password) {
      this.authService.newPassword(this.email, this.token, password).subscribe(
        {
          next: _ => {
            this.toastService.showSuccess("Your password has been reset successfully, you can now login!");
            this.router.navigate([""])
          },
          error: error => {
            this.toastService.showError(error.error.message)
          }
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
}
