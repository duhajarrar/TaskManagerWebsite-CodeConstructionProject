package com.bzu.taskmanager.service;
import org.springframework.stereotype.Service;

import com.bzu.taskmanager.model.*;
import com.bzu.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.List;
import java.time.LocalDate;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationService notificationService;

    public Task getByOwnerAndTaskId(String taskId, String owner) {
        return taskRepository.findByOwnerAndTaskId(owner,taskId);
    }

    public List<Task> getTasksByOwnerOrAssignee(String owner, String assignee){
         return taskRepository.findByOwnerOrAssignee(owner,assignee);
    }

    public List<Task> getTaskByOwnerOrAssigneeAndPriorityLevel(String priorityLevel, String owner, String assignee) {
        return taskRepository.findByOwnerOrAssigneeAndPriorityLevel(owner,assignee,priorityLevel);
    }

    public List<Task> getTaskByOwnerOrAssigneeAndDueDate(LocalDate dueDate, String owner, String assignee) {
        return taskRepository.findByOwnerOrAssigneeAndDueDate(owner,assignee,dueDate);
    }

    public List<Task> getTaskByOwnerAndAssignee(String owner , String assignee) {
        return taskRepository.findByOwnerAndAssignee(owner,assignee);
    }
    
    public List<Task> getTaskByOwnerOrAssigneeAndStatus(String status, String owner, String assignee) {
        return taskRepository.findByOwnerOrAssigneeAndStatus(owner,assignee,status);
    }
    
    
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    public List<Task> getByReminderDate(LocalDate today) {
        return taskRepository.findByReminderDate(today);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    public Task updateTask(Task task, String taskId, String userId) {
        Task existingTask = taskRepository.findByTaskId(taskId);
        if(existingTask == null || (!userId.equals(existingTask.getOwner()) && !userId.equals(existingTask.getAssignee())))
            return null;
        else{
            existingTask.setTaskName(task.getTaskName());
            existingTask.setDescription(task.getDescription());
            existingTask.setTags(task.getTags());
            existingTask.setPriorityLevel(task.getPriorityLevel());
            existingTask.setPreRequisites(task.getPreRequisites());
            existingTask.setAssignee(task.getAssignee());
            existingTask.setDueDate(task.getDueDate());
            existingTask.setCategory(task.getCategory());
            existingTask.setStatus(task.getStatus());
            existingTask.setAchievedPercentage(task.getAchievedPercentage());
            existingTask.setReminderDate(task.getReminderDate());
            existingTask.setCreatedDate(task.getCreatedDate());
            existingTask.setModifiedDate(LocalDate.now());
        }    
        return taskRepository.save(existingTask);    
    }

    public Task setCategory(String taskId, String category, String userId) {
        Task existingTask = taskRepository.findByTaskId(taskId);
        if(existingTask == null || (!userId.equals(existingTask.getOwner()) && !userId.equals(existingTask.getAssignee())))
            return null;
        existingTask.setCategory(category);
        existingTask.setModifiedDate(LocalDate.now());
        return taskRepository.save(existingTask);
    }
    
    public Task setStatus(String taskId, String status, String userId) {
        Task existingTask = taskRepository.findByTaskId(taskId);
        if(existingTask == null || (!userId.equals(existingTask.getOwner()) && !userId.equals(existingTask.getAssignee())))
            return null;
        existingTask.setStatus(status);
        existingTask.setModifiedDate(LocalDate.now());
        return taskRepository.save(existingTask);
    }

    public Task setAchievedPercentage(String taskId, int achievedPercentage, String userId) {
        Task existingTask = taskRepository.findByTaskId(taskId);
        if(existingTask == null || (!userId.equals(existingTask.getOwner()) && !userId.equals(existingTask.getAssignee())))
            return null;
        existingTask.setAchievedPercentage(achievedPercentage);
        existingTask.setModifiedDate(LocalDate.now());
        return taskRepository.save(existingTask);
    }

    public boolean deleteByTaskId(String taskId, String userId) {
        Task existingTask = taskRepository.findByTaskId(taskId);
        if(existingTask == null || (!userId.equals(existingTask.getOwner()) && !userId.equals(existingTask.getAssignee())))
            return false;
        else{

            taskRepository.deleteByTaskId(taskId);
            return true;
        }
    }
    public List<Task> findByStatusAndReminderDateBefore(String status, LocalDate currentDate){
       return taskRepository.findByStatusAndReminderDateBefore(status,currentDate);
    }
    public Task setPreRequisites(String taskId, List<String> preRequisites, String userId) {
        Task existingTask = taskRepository.findByTaskId(taskId);
        if(existingTask == null || (!userId.equals(existingTask.getOwner()) && !userId.equals(existingTask.getAssignee())))
            return null;
        existingTask.setPreRequisites(preRequisites);
        existingTask.setModifiedDate(LocalDate.now());
        return taskRepository.save(existingTask);
    }

    public Task setPriority(String taskId, String priorityLevel, String userId) {
        Task existingTask = taskRepository.findByTaskId(taskId);
        if(existingTask == null || (!userId.equals(existingTask.getOwner()) && !userId.equals(existingTask.getAssignee())))
            return null;
        existingTask.setPriorityLevel(priorityLevel);
        existingTask.setModifiedDate(LocalDate.now());
        return taskRepository.save(existingTask);
    }

    public Task setDueDate(String taskId, LocalDate dueDate, String userId) {
        Task existingTask = taskRepository.findByTaskId(taskId);
        if(existingTask == null || (!userId.equals(existingTask.getOwner()) && !userId.equals(existingTask.getAssignee())))
            return null;
        existingTask.setDueDate(dueDate);
        existingTask.setModifiedDate(LocalDate.now());
        return taskRepository.save(existingTask);
    }

    public Task setReminderDate(String taskId, LocalDate reminderDate, String userId) {
        Task existingTask = taskRepository.findByTaskId(taskId);
        if(existingTask == null || (!userId.equals(existingTask.getOwner()) && !userId.equals(existingTask.getAssignee())))
            return null;
        existingTask.setReminderDate(reminderDate);
        existingTask.setModifiedDate(LocalDate.now());
        return taskRepository.save(existingTask);
    }

    public List<Task> searchTasks(String keyword, String userId) {
        return taskRepository.findByOwnerOrAssigneeAndTaskNameContainingIgnoreCase(userId,userId,keyword);
    }

    public List<Task> getTasksByUser(String userId) {
        return taskRepository.findByAssignee(userId);
    }
    
    public void sendTaskReminderEmail(Task task) {
    }

    public Task markCompleted(String taskId, String userId) {
        
        Task existingTask = taskRepository.findByTaskId(taskId);
        if(existingTask == null || (!userId.equals(existingTask.getOwner()) && !userId.equals(existingTask.getAssignee())))
            return null;
        existingTask.setCompleted(true);
        existingTask.setAchievedPercentage(100);
        existingTask.setModifiedDate(LocalDate.now());
        existingTask.setCompletionDate(LocalDate.now());
        String message = "The task '" + existingTask.getTaskName() + "' is completed.";
        notificationService.sendNotification(existingTask,existingTask.getAssignee(), message);
         if(!(existingTask.getOwner()).equals(existingTask.getAssignee()))
        notificationService.sendNotification(existingTask,existingTask.getOwner(), message);

        return taskRepository.save(existingTask);
        
    }

    // @Scheduled(fixedRate = 60000) // Check every minute
    @Scheduled(cron = "0 0 7 * * *") // Runs every day at 7 AM
    public void checkTaskDeadlines() {
        LocalDate currentDate = LocalDate.now();
        List<Task> approachingDeadlineTasks = taskRepository.findByDueDateBefore(currentDate.plusDays(1));
        
        for (Task task : approachingDeadlineTasks) {
            String message = "The task '" + task.getTaskName() + "' is approaching its deadline.";
            notificationService.sendNotification(task,task.getAssignee(),message);

            if(!(task.getOwner()).equals(task.getAssignee()))
                notificationService.sendNotification(task,task.getOwner(),message);
        }
    }
    
    public Task markIncompleted(String taskId, int achievedPercentage, String userId) {
        Task existingTask = taskRepository.findByTaskId(taskId);
        if(existingTask == null || (!userId.equals(existingTask.getOwner()) && !userId.equals(existingTask.getAssignee())))
            return null;
        existingTask.setCompleted(false);
        existingTask.setAchievedPercentage(achievedPercentage);
        existingTask.setModifiedDate(LocalDate.now());
        return taskRepository.save(existingTask);
    }

}
