package com.bzu.taskmanager.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bzu.taskmanager.model.Task;
import com.bzu.taskmanager.repository.TaskRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private TaskRepository taskRepository;

    public double calculateCompletionRate(String userId) {
        int totalTasks = taskRepository.countByAssignee(userId);
        int completedTasks = taskRepository.countByAssigneeAndCompletedTrue(userId);
        
        if (totalTasks == 0) {
            return 0.0;
        }

        return (double) completedTasks / totalTasks * 100;
    }
    
    public long calculateAverageCompletionTime(String userId) {
        List<Task> completedTasks = taskRepository.findByAssigneeAndCompletedTrue(userId);

        if (completedTasks.isEmpty()) {
            return 0;
        }

        long totalDays = 0;

        for (Task task : completedTasks) {
            LocalDate creationDate = task.getCreatedDate();
            LocalDate completionDate = task.getCompletionDate();

            long daysBetween = ChronoUnit.DAYS.between(creationDate, completionDate);
            totalDays += daysBetween;
        }

        long averageDays = totalDays / completedTasks.size();
        return averageDays;
    }
}
