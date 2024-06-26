import { Component, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { UserService } from '../services/user/user.service';
import { Task, TaskDialogData } from '../models/task.model';
import { ToastrService } from 'ngx-toastr';
import { TaskService } from '../services/task/task.service';
import { NewTaskComponent } from '../task/task-dialog/task-dialog.component';
import { FullCalendarModule } from '@fullcalendar/angular'; // useful for typechecking
import dayGridPlugin from '@fullcalendar/daygrid';
import { TaskPriority, TaskStatus } from '../constants/enum/task.enum';

@Component({
  selector: 'app-tasklisting',
  templateUrl: './tasklisting.component.html',
  styleUrls: ['./tasklisting.component.scss'],
})
export class TasklistingComponent {
  constructor(
    private service: UserService,
    private matDialog: MatDialog,
    private taskService: TaskService,
    private toastr: ToastrService
  ) {
    this.LoadTasks();
  }

  openNewTaskDialog(actionType: string, taskId: string) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { actionType, taskId } as TaskDialogData;
    this.matDialog.open(NewTaskComponent, dialogConfig);

    this.matDialog.afterAllClosed.subscribe(() => {
      this.LoadTasks();
    });
  }

  deleteTask(taskId: string) {
    this.taskService.DeleteTask(taskId).subscribe(
      () => {
        this.toastr.success('Task deleted successfully!');
        this.LoadTasks();
      },
      (err) => {
        console.log(err, 'error');
        this.toastr.error(`Can't delete task`);
      }
    );
  }

  isCalendarView: boolean = false;
  taskList: Task[] = [];
  groupedList: any = [];
  subGroup: string | undefined = '';
  dataSource: any;
  displayedColumns: string[] = [
    'taskId',
    'taskName',
    'tags',
    'description',
    'priorityLevel',
    'status',
    'assignee',
    'dueDate',
    'category',
    'actions',
  ];
  prioritiesLevels = Object.values(TaskPriority);
  tasksStatuses = Object.values(TaskStatus);
  categories = [''];

  calendarOptions: FullCalendarModule = {
    initialView: 'dayGridMonth',
    plugins: [dayGridPlugin],
    eventClick: this.handleEventClick.bind(this),
  };

  handleEventClick(info: any): void {
    const event = info.event;
    this.openNewTaskDialog('Edit', event.id);
  }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  LoadTasks() {
    this.service.GetUserTasks().subscribe((tasks: Task[]) => {
      this.taskList = tasks;
      this.categories = [...new Set(tasks.map((task) => task.category))];
      this.dataSource = new MatTableDataSource<Task>(this.taskList);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      if (this.view === 'board' || this.view === 'calendar')
        this.updateGroupedList();
    });
  }

  setGroupBy(name: string, subGroup?: string) {
    this.group_by = name;
    this.subGroup = subGroup;
    this.updateGroupedList(subGroup);
  }

  group_by = 'status';
  groups: any[] = [];
  view = 'board'; //table or board
  filteredTaskList: Task[] = [];
  isSearch: boolean = false;

  filterResults(text: string) {
    debugger;
    if (!text) {
      this.isSearch = false;
      this.filteredTaskList = [];
    } else {
      this.isSearch = true;
    }

    this.filteredTaskList = this.taskList.filter((task) =>
      task?.taskName.toLowerCase().includes(text.toLowerCase())
    );
    this.updateGroupedList();
  }

  GroupBy(myArray: Task[], GroupBy: string, subGroup?: string): {} {
    const groupedTasks = myArray.reduce((result: any, task: Task) => {
      if (GroupBy === 'status') {
        if (this.isCalendarView) {
          if (task.status === subGroup) {
            if (!result[task.status]) {
              result[task.status] = [];
            }
            result[task.status].push(task);
          }
        } else {
          if (!result[task.status]) {
            result[task.status] = [];
          }
          result[task.status].push(task);
        }
      } else if (GroupBy === 'achievedPercentage') {
        if (!result[task.achievedPercentage]) {
          result[task.achievedPercentage] = [];
        }
        result[task.achievedPercentage].push(task);
      } else if (GroupBy === 'priorityLevel') {
        if (this.isCalendarView) {
          if (task.priorityLevel === subGroup) {
            if (!result[task.priorityLevel]) {
              result[task.priorityLevel] = [];
            }
            result[task.priorityLevel].push(task);
          }
        } else {
          if (!result[task.priorityLevel]) {
            result[task.priorityLevel] = [];
          }
          result[task.priorityLevel].push(task);
        }
      } else if (GroupBy === 'category') {
        if (!result[task.category]) {
          result[task.category] = [];
        }
        result[task.category].push(task);
      } else {
        if (!result['Uncategoriesed']) {
          result['Uncategoriesed'] = [];
        }
        result['Uncategoriesed'].push(task);
      }
      return result;
    }, {});

    this.groups = Object.keys(groupedTasks);

    return groupedTasks;
  }

  updateGroupedList(subGroup?: string) {
    debugger;
    this.groupedList = this.isSearch
      ? this.GroupBy(this.filteredTaskList, this.group_by, subGroup)
      : this.GroupBy(this.taskList, this.group_by, subGroup);
    const tasks = Object.values(this.groupedList);
    let filteredTasks;
    //@ts-ignore
    const allTasks = [].concat(...tasks);
    if (!subGroup) {
      filteredTasks = allTasks.map((task: Task) => {
        return {
          title: task.taskName,
          date: new Date(task.dueDate).toISOString().split('T')[0],
          id: task.taskId,
        };
      });
    } else {
      filteredTasks = this.groupedList[subGroup].map((task: Task) => {
        return {
          title: task.taskName,
          date: new Date(task.dueDate).toISOString().split('T')[0],
          id: task.taskId,
        };
      });
    }
    //@ts-ignore
    this.calendarOptions.events = filteredTasks;
  }

  drop(event: CdkDragDrop<Task[]>, updatedValue: string) {
    if (event.previousContainer !== event.container) {
      debugger;
      let item = event.previousContainer.data[event.previousIndex];
      debugger;
      this.taskList = this.taskList.map((task) => {
        if (task.taskId === item.taskId) {
          return {
            ...task,
            [this.group_by]: updatedValue,
          };
        }
        return task;
      });

      this.updateGroupedList();
    }
  }
}
