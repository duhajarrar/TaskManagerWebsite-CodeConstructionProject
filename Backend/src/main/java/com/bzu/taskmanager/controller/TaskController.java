package com.bzu.taskmanager.controller;

import com.bzu.taskmanager.model.*;
import com.bzu.taskmanager.payload.response.MessageResponse;
import com.bzu.taskmanager.security.services.UserDetailsImpl;
import com.bzu.taskmanager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;
@Controller
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private EmailService emailService;

    @Autowired
	PasswordEncoder encoder;
    
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        task.setOwner(userDetails.getUsername());
        
        if(task.getAssignee() == null)
            task.setAssignee(userDetails.getUsername());

        if(task.getCreatedDate() == null)
           task.setCreatedDate(LocalDate.now());
        
        Task newTask = taskService.createTask(task);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("getTask/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Task> getByOwnerAndTaskId(@PathVariable String taskId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        Task task = taskService.getByOwnerAndTaskId(taskId,userDetails.getUsername());
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/tasks")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Task>> getTasksByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        List<Task> tasks = taskService.getTasksByOwnerOrAssignee(userDetails.getUsername(),userDetails.getUsername());
        if (tasks.size() <= 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("update/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Task> updateTask(@PathVariable String taskId, @RequestBody Task task) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Task updatedTask = taskService.updateTask(task,taskId,userDetails.getUsername());
        if (updatedTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("delete/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        boolean deleted = taskService.deleteByTaskId(taskId,userDetails.getUsername());
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("filterByPriority/{priorityLevel}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Task>> filterByPriorityLevel(@PathVariable String priorityLevel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        List<Task> tasks = taskService.getTaskByOwnerOrAssigneeAndPriorityLevel(priorityLevel,userDetails.getUsername(),userDetails.getUsername());
        if (tasks.size() <= 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("filterByDueDate/{dueDate}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Task>> filterByDueDate(@PathVariable LocalDate dueDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        List<Task> tasks = taskService.getTaskByOwnerOrAssigneeAndDueDate(dueDate,userDetails.getUsername(),userDetails.getUsername());
        if (tasks.size() <= 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("filterByAssignee/{assignee}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Task>> filterByAssignee(@PathVariable String assignee) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        List<Task> tasks = taskService.getTaskByOwnerAndAssignee(userDetails.getUsername(),assignee);
        if (tasks.size() <= 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("filterByStatus/{status}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Task>> filterByStatus(@PathVariable String status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        List<Task> tasks = taskService.getTaskByOwnerOrAssigneeAndStatus(status,userDetails.getUsername(),userDetails.getUsername());
        if (tasks.size() <= 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    
    @PutMapping("setDueDate/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Task> updateDueDate(@PathVariable String taskId, @RequestBody LocalDate dueDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Task updatedTask = taskService.setDueDate(taskId,dueDate,userDetails.getUsername());
        if (updatedTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @PutMapping("setPriority/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Task> updatePriority(@PathVariable String taskId, @RequestBody String priorityLevel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Task updatedTask = taskService.setPriority(taskId,priorityLevel,userDetails.getUsername());
        if (updatedTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @PutMapping("setPreRequisites/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Task> updatePreRequisites(@PathVariable String taskId, @RequestBody List<String> preRequisites) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Task updatedTask = taskService.setPreRequisites(taskId,preRequisites,userDetails.getUsername());
        if (updatedTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @PutMapping("assignCategory/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Task> assignCategory(@PathVariable String taskId, @RequestBody String category) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Task updatedTask = taskService.setCategory(taskId,category,userDetails.getUsername());
        if (updatedTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @PutMapping("setReminderDate/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Task> updateReminderDate(@PathVariable String taskId, @RequestBody LocalDate reminderDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Task updatedTask = taskService.setReminderDate(taskId,reminderDate,userDetails.getUsername());
        if (updatedTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @GetMapping("/search") // this search using subtitle of task
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam String query) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        List<Task> tasks = taskService.searchTasks(query,userDetails.getUsername());
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("setStatus/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Task> updateStatus(@PathVariable String taskId, @RequestBody String status,@RequestBody int  achievedPercentage) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Task updatedTask = taskService.setStatus(taskId,status,userDetails.getUsername());
        updatedTask = taskService.setAchievedPercentage(taskId,achievedPercentage,userDetails.getUsername());

        if (updatedTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @PutMapping("markCompleted/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Task> markCompleted(@PathVariable String taskId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        
        Task updatedTask = taskService.markCompleted(taskId,userDetails.getUsername());
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @PutMapping("markIncompleted/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Task> markIncompleted(@PathVariable String taskId,@RequestBody int  achievedPercentage) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Task updatedTask = taskService.markIncompleted(taskId,achievedPercentage,userDetails.getUsername());
        if (updatedTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @GetMapping("/tasks/{taskId}/sendReminder")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> sendReminderEmail(@PathVariable("taskId") String taskId, @RequestParam("email") String email) {
        String subject = "Reminder: Task #" + taskId;
        String body = "Dear user, this is a reminder for your task: [Task Details]";
        emailService.sendEmail(email, subject, body);

		return ResponseEntity.ok(new MessageResponse("Sent successfully!"));
    }

}
