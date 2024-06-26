package com.bzu.taskmanager.controller;
import java.time.LocalDate;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.bzu.taskmanager.model.Task;
import com.bzu.taskmanager.model.User;
import com.bzu.taskmanager.repository.UserRepository;
import com.bzu.taskmanager.service.EmailService;
import com.bzu.taskmanager.service.TaskService;;
@Component
public class EmailScheduler {
    @Autowired
    private EmailService emailService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 7 * * *") // Runs every day at 7 AM
//    @Scheduled(fixedDelay = 600000) // Runs every minute
    public void sendDailyReminder() {
        LocalDate today = LocalDate.now();
        List<Task> tasks = taskService.getByReminderDate(today);
        
        if(tasks.size() <= 0 )
            return ;
        
        for (Task task : tasks) {
                if(!(task.getAssignee()).equals("")){
                User user = userRepository.findByUsername(task.getAssignee())
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + task.getAssignee()));

                    String to = user.getEmail();
                    
                    String subject = "Task Reminder";
                    String text = "This is a reminder for your task: " + task.getTaskName()+ " , the deadline is in "+ task.getDueDate()+" try to finish it ASAP";

                    emailService.sendEmail(to, subject, text);
                }
            
            if(!(task.getOwner()).equals(task.getAssignee()) && !(task.getOwner()).equals("")){
               
                User user = userRepository.findByUsername(task.getOwner())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + task.getOwner()));

                String to = user.getEmail();
                

                String subject = "Task Reminder";
                String text = "This is a reminder for the task you created: " + task.getTaskName()+ " , the deadline is in "+ task.getDueDate()+" ,we need to notify you that task completed percentege is "+ task.getAchievedPercentage();

                emailService.sendEmail(to, subject, text);
                
            }
        }
    }
}
