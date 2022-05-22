import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";

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

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
  }

  onSubmit() {
    this.submitted = true;
    console.log(this.forgotPasswordForm.get("email")?.value)
  }
}
