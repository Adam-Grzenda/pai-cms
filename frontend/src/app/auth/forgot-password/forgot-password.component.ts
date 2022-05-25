import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {AuthService} from "../auth.service";
import {ToastService} from "../../toast.service";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  forgotPasswordForm = this.fb.group(
    {
      email: ['', [Validators.required, Validators.email]]
    }
  );
  submitted: boolean = false;
  success: boolean = false;
  loading: boolean = false;

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private toastService: ToastService) {
  }

  ngOnInit(): void {
  }

  onSubmit() {
    this.loading = true;
    this.authService.forgottenPassword(this.forgotPasswordForm.get("email")?.value).subscribe(
      {
        next: (value) => {
          this.submitted = true;
          this.success = value;
          this.loading = false;
        },
        error: (error) => {
          if(error.status == 404) {
            this.submitted = true;
            this.success = false;
          } else {
            this.toastService.showUnexpectedError();
          }
          this.loading = false;
        }
      }
    )
  }
}
