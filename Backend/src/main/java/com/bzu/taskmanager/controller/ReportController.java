package com.bzu.taskmanager.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import com.bzu.taskmanager.security.services.UserDetailsImpl;
import com.bzu.taskmanager.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/completion-rate/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public double getCompletionRate() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return reportService.calculateCompletionRate(userDetails.getUsername());
    }

    @GetMapping("/average-completion-time/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public long getAverageCompletionTime() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return reportService.calculateAverageCompletionTime(userDetails.getUsername());
    }


    @GetMapping("/completion-rate/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public double getCompletionRate(@PathVariable String userId) {
        return reportService.calculateCompletionRate(userId);
    }

    @GetMapping("/average-completion-time/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public long getAverageCompletionTime(@PathVariable String userId) {
        return reportService.calculateAverageCompletionTime(userId);
    }
}
