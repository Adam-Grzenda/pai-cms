import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, ValidationErrors, Validators} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  login: boolean = false;

  registrationForm = this.fb.group(
    {
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeated: ['', [Validators.required, this.passwordMatching]]
    }
  );

  constructor(private fb: FormBuilder) {
  }

  ngOnInit(): void {
  }

  onSubmit() {
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
}
