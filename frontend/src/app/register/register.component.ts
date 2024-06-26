import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../services/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  constructor(
    private builder: FormBuilder,
    private toastr: ToastrService,
    private service: AuthService,
    private router: Router
  ) {}

  registerForm = this.builder.group({
    username: this.builder.control(
      '',
      Validators.compose([Validators.required, Validators.minLength(5)])
    ),
    name: this.builder.control('', Validators.required),
    password: this.builder.control(
      '',
      Validators.compose([
        Validators.required,
        // Validators.pattern(
        //   '(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-zd$@$!%*?&].{8,}'
        // ),
      ])
    ),
    email: this.builder.control(
      '',
      Validators.compose([Validators.required, Validators.email])
    ),
    gender: this.builder.control('male'),
  });

  proceedRegistration() {
    if (this.registerForm.valid) {
      this.service
        .register(
          this.registerForm.value.username || '',
          this.registerForm.value.email || '',
          this.registerForm.value.password || ''
        )
        .subscribe((res: any) => {
          this.toastr.success(
            'Please ask admin for activation',
            'registered successfully'
          );
          this.router.navigate(['login']);
        });
    } else {
      this.toastr.warning('Please enter valid data');
    }
  }
}
