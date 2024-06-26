package com.bzu.taskmanager.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.bzu.taskmanager.model.Task;

import java.time.LocalDate;
import java.util.*;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    Task findByTaskId(String taskId);
    void deleteByTaskId(String taskId);
    List<Task> findAll();
    List<Task> findByPriorityLevel(String priorityLevel);
    List<Task> findTaskByAssignee(String assignee);
    List<Task> findByAssignee(String userId);
    List<Task> findByTaskNameContainingIgnoreCase(String taskName);
    List<Task> findByOwnerOrAssigneeAndTaskNameContainingIgnoreCase(String owner, String assignee, String taskName);
    @Query("{ $expr: { $eq: [{ $toDate: '$reminderDate' }, ?0] } }")
    List<Task> findByReminderDate(LocalDate reminderDate);
    List<Task> findByStatus(String status);
    List<Task> findByReminderDateBefore(LocalDate reminderDate);
    @Query("{ 'duedate' : { $lt: ?0 }}")
    List<Task> findByCompletedFalseAndDueDate(LocalDate dueDate);
    List<Task> findByStatusAndReminderDateBefore(String status,LocalDate reminderDate);
    List<Task> findByCompleted(Boolean completed);
    List<Task> findByOwnerOrAssigneeAndDueDate(String owner, String assignee, LocalDate dueDate);
    List<Task> findByOwnerOrAssigneeAndStatus(String owner, String assignee, String status);
    List<Task> findByOwnerAndAssignee(String owner, String assignee);
    @Query("{ $expr: { $eq: [{ $toDate: '$dueDate' }, ?0] } }")
    List<Task> findByDueDateBefore(LocalDate dueDate);
    @Query("{ $expr: { $eq: [{ $toDate: '$dueDate' }, ?0] } }")    
    List<Task> findByDueDate(LocalDate dueDate);

    int countByAssignee(String assignee);
    int countByAssigneeAndCompletedTrue(String assignee);
    List<Task> findByAssigneeAndCompletedTrue(String assignee);
    List<Task> findByOwner(String owner);
    List<Task> findByOwnerOrAssignee(String owner, String assignee);
    Task findByOwnerAndTaskId(String owner, String taskId);
    Optional<Task> findByTaskIdAndOwnerOrAssignee(String taskId, String owner, String assignee);
    List<Task> findByOwnerOrAssigneeAndPriorityLevel(String owner, String assignee, String priorityLevel);


}
