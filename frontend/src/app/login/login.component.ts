import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../services/auth/auth.service';
import { Router } from '@angular/router';
import { StorageService } from '../services/storage/storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  errorMessage = '';
  roles: string[] = [];

  constructor(
    private builder: FormBuilder,
    private toastr: ToastrService,
    private service: AuthService,
    private storage: StorageService,
    private router: Router
  ) {
    this.storage.clean();
  }

  ngOnInit(): void {
    if (this.storage.IsLoggedIn()) {
      this.roles = this.storage.GetUserRole();
    }
  }

  userData: any;
  loginForm = this.builder.group({
    username: this.builder.control('', Validators.required),
    password: this.builder.control('', Validators.required),
  });

  proceedLogin() {
    if (this.loginForm.valid) {
      this.service
        .login(
          this.loginForm.value.username || '',
          this.loginForm.value.password || ''
        )
        .subscribe({
          next: (data) => {
            this.storage.saveUser(data);
            this.storage.saveToken(data.accessToken);
            this.storage.saveRefreshToken(data.refreshToken);
            this.router.navigate(['']);
          },
          error: (err) => {
            this.errorMessage = err.error.message;
            this.toastr.error('Invalid Credentials');
          },
        });
    } else {
      this.toastr.warning('Please enter valid data');
    }
  }
}
