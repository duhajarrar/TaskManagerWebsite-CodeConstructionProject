import { Component, OnInit, Input, Inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { TaskFormGroup, Task, TaskDialogData } from '../../models/task.model';
import { TaskService } from '../../services/task/task.service';
import { UserService } from '../../services/user/user.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { TaskPriority, TaskStatus } from '../../constants/enum/task.enum';
import { Observable, startWith, map } from 'rxjs';
import { User } from '../../models/user.model';
import { FormControl } from '@angular/forms';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'app-task-dialog',
  templateUrl: './task-dialog.component.html',
  styleUrls: ['./task-dialog.component.scss'],
})
export class NewTaskComponent implements OnInit {
  formcontrol = new FormControl('');
  TaskPriority = TaskPriority;
  TaskStatus = TaskStatus;
  UsersList!: User[];
  filterUserslist!: Observable<User[]>;

  constructor(
    private builder: FormBuilder,
    private taskService: TaskService,
    private authService: UserService,
    private toastr: ToastrService,
    private dialogRef: MatDialogRef<NewTaskComponent>,
    @Inject(MAT_DIALOG_DATA) public data: TaskDialogData
  ) {
    if (data.actionType === 'Edit') {
      this.getTaskData(data.taskId);
    }
    this.UsersList = [];
  }

  ngOnInit(): void {
    this.filterUserslist = this.formcontrol.valueChanges.pipe(
      startWith(''),
      map((user) => this._UsersILTER(user || ''))
    );
  }

  taskForm = this.builder.group({
    taskId: this.builder.control(null),
    taskName: this.builder.control('', Validators.required),
    tags: this.builder.control([]),
    description: this.builder.control(''),
    priorityLevel: this.builder.control(TaskPriority.low, Validators.required),
    preRequisites: this.builder.control([]),
    status: this.builder.control(TaskStatus.open),
    assignee: this.builder.control(''),
    dueDate: this.builder.control(new Date()),
    category: this.builder.control('', Validators.required),
    reminderDate: this.builder.control(new Date()),
    createdDate: this.builder.control(new Date()),
    modifiedDate: this.builder.control(new Date()),
    completed: this.builder.control(false),
    achievedPercentage: this.builder.control(0),
  }) as TaskFormGroup;

  submitTask() {
    debugger;
    if (!this.taskForm.valid) {
      this.toastr.error('Please enter valid data');
      return;
    }
    this.taskForm.controls.taskName.setValue(
      this.taskForm.controls.taskName.value.trim()
    );
    this.taskForm.controls.description.setValue(
      this.taskForm.controls.description.value.trim()
    );
    if (
      this.taskForm.value.tags?.length > 0 &&
      typeof this.taskForm.value.tags !== 'object'
    ) {
      const tags = this.taskForm.value.tags?.split(',');
      this.taskForm.controls.tags.setValue(
        tags.map((tag: string) => tag.trim())
      );
    }

    if (this.taskForm.value.preRequisites?.length > 0) {
      const preRequisites = this.taskForm.value?.preRequisites.split(',');
      this.taskForm.controls.preRequisites.setValue(
        preRequisites.map((prerequisite: string) => prerequisite.trim())
      );
    }

    if (this.data.actionType === 'Edit') {
      this.taskService.updateTask(this.taskForm.value).subscribe(
        (task: Task) => {
          this.toastr.success(`${task.taskName} updated successfully!`);
          this.dialogRef.close();
        },
        (err) => {
          console.log(err, 'error');
          this.toastr.error('Please enter valid data');
        }
      );
    } else {
      this.taskService.AddTask(this.taskForm.value).subscribe(
        (task: Task) => {
          this.toastr.success(`${task.taskName} added successfully!`);
          this.dialogRef.close();
        },
        (err) => {
          console.log(err, 'error');
          this.toastr.error('Please enter valid data');
        }
      );
    }
  }

  private _UsersILTER(value: string): User[] {
    const searchvalue = value.toLocaleLowerCase();
    return this.UsersList.filter(
      (user) =>
        user.name.toLocaleLowerCase().includes(searchvalue) ||
        user.name.toLocaleLowerCase().includes(searchvalue)
    );
  }

  getTaskData(taskId: string) {
    debugger;
    this.taskService.GetTaskById(taskId).subscribe((taskData: Task) => {
      console.log(taskData.taskId);
      this.taskForm.setValue({
        taskId: taskData.taskId,
        taskName: taskData.taskName,
        priorityLevel: taskData.priorityLevel,
        description: taskData.description,
        tags: taskData.tags, // should be edited in BE
        category: taskData.category,
        dueDate: taskData.dueDate,
        preRequisites: taskData.preRequisites,
        status: taskData.status,
        assignee: taskData.assignee, // should be edited in BE,
        reminderDate: taskData.reminderDate,
        createdDate: taskData.createdDate,
        modifiedDate: taskData.modifiedDate,
        completed: taskData.completed,
        achievedPercentage: taskData.achievedPercentage,
      });
    });
  }
}
