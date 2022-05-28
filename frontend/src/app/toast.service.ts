import {Injectable} from '@angular/core';
import {ToastrService} from "ngx-toastr";

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  constructor(private toastr: ToastrService) {
  }


  showUnexpectedError() {
    this.toastr.error("Try again later.", "Something went wrong!")
  }

  showError(errorMessage: string) {
    this.toastr.error(errorMessage, "Something went wrong!")
  }

  showSuccess(message: string) {
    this.toastr.success(message)
  }
}
