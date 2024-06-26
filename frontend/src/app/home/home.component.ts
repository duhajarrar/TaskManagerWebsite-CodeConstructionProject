import { Component, OnInit } from '@angular/core';
import { ChartData, ChartType } from 'chart.js';
import { ChartEvent } from 'chart.js/dist/core/core.plugins';
import { Task } from '../models/task.model';
import { UserService } from '../services/user/user.service';
import { TaskStatus } from '../constants/enum/task.enum';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  taskCountsByStatus: Record<TaskStatus, number> = {
    [TaskStatus.todo]: 0,
    [TaskStatus.closed]: 0,
    [TaskStatus.open]: 0,
    [TaskStatus.inprogress]: 0,
    [TaskStatus.done]: 0,
  };
  taskCompletionRate: Record<string, number> = {
    Completed: 0,
    Incomplete: 0,
  };

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.GetUserTasks().subscribe((tasks: Task[]) => {
      debugger;
      tasks.forEach((task) => {
        this.taskCountsByStatus[task.status]++;
      });
      tasks.forEach((task) => {
        if (task.completed) {
          this.taskCompletionRate['Completed']++;
        } else {
          this.taskCompletionRate['Incomplete']++;
        }
      });

      this.taskCompletionRateData = {
        labels: Object.keys(this.taskCompletionRate),
        datasets: [
          {
            data: Object.values(this.taskCompletionRate),
            label: 'Task Completion Rate',
          },
        ],
      };

      this.taskByStatusData = {
        labels: Object.keys(this.taskCountsByStatus),
        datasets: [
          {
            data: Object.values(this.taskCountsByStatus),
            label: 'Tasks By Status',
          },
        ],
      };
    });
  }

  public taskByStatusLegend = true;
  public taskByStatusChartType: ChartType = 'polarArea';
  public taskByStatusData: ChartData<'polarArea'> = {
    labels: Object.keys(this.taskCountsByStatus),
    datasets: [
      {
        data: Object.values(this.taskCountsByStatus),
        label: 'Tasks By Status',
      },
    ],
  };

  public taskCompletionRateLegend = true;
  public taskCompletionRateChartType: ChartType = 'doughnut';
  public taskCompletionRateData: ChartData<'doughnut'> = {
    labels: Object.keys(this.taskCompletionRate),
    datasets: [
      {
        data: Object.values(this.taskCompletionRate),
        label: 'Task Completion Rate',
      },
    ],
  };
}
