import {Injectable} from '@angular/core';
import {ToastrService} from "ngx-toastr";

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  constructor(private toastr: ToastrService) {
  }


  showUnexpectedError() {
    this.toastr.error("Something went wrong!", "Try again later.")
  }

  showError(errorMessage: string) {
    this.toastr.error("Something went wrong!", errorMessage)
  }
}
