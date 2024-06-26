import { FormGroup, AbstractControl } from '@angular/forms';
import { TaskPriority, TaskStatus } from '../constants/enum/task.enum';
import { User } from '../models/user.model';

interface Task {
  taskId?: string;
  taskName: string;
  priorityLevel: TaskPriority;
  tags: string[];
  description: string;
  status: TaskStatus;
  dueDate: Date;
  preRequisites: string[];
  assignee: String;
  category: string;
  reminderDate: Date;
  createdDate: Date;
  modifiedDate: Date;
  completed: boolean;
  achievedPercentage: number;
}

type TaskControls = { [key in keyof Task]: AbstractControl };

type TaskFormGroup = FormGroup & {
  value: Task;
  controls: TaskControls;
};

interface TaskDialogData {
  actionType: string;
  taskId: string;
}

export { TaskFormGroup, Task, TaskDialogData };
