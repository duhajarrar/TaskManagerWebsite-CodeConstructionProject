import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../services/user/user.service';

@Component({
  selector: 'app-updatepopup',
  templateUrl: './updatepopup.component.html',
  styleUrls: ['./updatepopup.component.scss'],
})
export class UpdatepopupComponent implements OnInit {
  constructor(
    private builder: FormBuilder,
    private service: UserService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private toastr: ToastrService,
    private dialog: MatDialogRef<UpdatepopupComponent>
  ) {}

  ngOnInit(): void {
    if (this.data.usercode != null && this.data.usercode != '') {
      this.service.GetByCode(this.data.usercode).subscribe((res) => {
        this.editdata = res;
        this.registerForm.setValue({
          id: this.editdata.id,
          name: this.editdata.name,
          password: this.editdata.password,
          email: this.editdata.email,
          gender: this.editdata.gender,
          role: this.editdata.role,
          isActive: this.editdata.isActive,
        });
      });
    }
  }
  rolelist: any[] = [
    {
      code: 'ROLE_ADMIN',
      name: 'Admin',
    },
    {
      code: 'ROLE_MODERATOR',
      name: 'Moderator',
    },
    {
      code: 'ROLE_USER',
      name: 'User',
    },
  ];
  editdata: any;

  registerForm = this.builder.group({
    id: this.builder.control(''),
    name: this.builder.control(''),
    password: this.builder.control(''),
    email: this.builder.control(''),
    gender: this.builder.control('male'),
    role: this.builder.control('', Validators.required),
    isActive: this.builder.control(false),
  });

  updateuser() {
    if (this.registerForm.valid) {
      this.service
        .UpdateUser(this.registerForm.value.id, this.registerForm.value)
        .subscribe((res) => {
          this.toastr.success('Updated Successfully');
          this.dialog.close();
        });
    } else {
      this.toastr.warning('Please Select Role');
    }
  }
}
