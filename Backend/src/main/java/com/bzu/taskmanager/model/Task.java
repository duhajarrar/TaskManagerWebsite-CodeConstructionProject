package com.bzu.taskmanager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "tasks")
public class Task {

    @Id
    private String taskId;
    private String taskName;
    private String owner;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> tags;
    private String description;
    private String priorityLevel;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> preRequisites;
    private String status;
    private String assignee;
    private LocalDate dueDate;
    private String category;
    private LocalDate reminderDate;
    private LocalDate createdDate;
    private LocalDate completionDate;
    private LocalDate modifiedDate;
    private boolean completed;
    private int achievedPercentage;

    public Task() {}

    public Task(String taskName, String description, String priorityLevel, List<String> preRequisites, String status, String assignee, LocalDate dueDate, LocalDate completionDate, String category, LocalDate reminderDate, LocalDate createdDate, LocalDate modifiedDate, boolean completed, int achievedPercentage, String owner, List<String> tags) {
        this.taskName = taskName;
        this.owner = owner;
        this.tags = tags;
        this.description = description;
        this.priorityLevel = priorityLevel;
        this.preRequisites = preRequisites;
        this.status = status;
        this.assignee = assignee;
        this.dueDate = dueDate;
        this.category = category;
        this.reminderDate = reminderDate;
        this.completionDate = completionDate;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.completed = completed;
        this.achievedPercentage = achievedPercentage;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTags(String tag) {
        this.tags.add(tag);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public List<String> getPreRequisites() {
        return preRequisites;
    }

    public void setPreRequisites(List<String> preRequisites) {
        this.preRequisites = preRequisites;
    }
    public void addPreRequisites(String preRequisitesTask) {
        this.preRequisites.add(preRequisitesTask);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getAchievedPercentage() {
        return achievedPercentage;
    }

    public void setAchievedPercentage(int achievedPercentage) {
        this.achievedPercentage = achievedPercentage;
    }


    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }



    public LocalDate getCompletionDate() {
        return this.completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public boolean isCompleted() {
        return this.completed;
    }
   

    @Override
    public String toString() {
        return "{" +
            " taskId='" + getTaskId() + "'" +
            ", taskName='" + getTaskName() + "'" +
            ", owner='" + getOwner() + "'" +
            ", tags='" + getTags() + "'" +
            ", description='" + getDescription() + "'" +
            ", priorityLevel='" + getPriorityLevel() + "'" +
            ", preRequisites='" + getPreRequisites() + "'" +
            ", status='" + getStatus() + "'" +
            ", assignee='" + getAssignee() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", category='" + getCategory() + "'" +
            ", reminderDate='" + getReminderDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", completionDate='" + getCompletionDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", completed='" + isCompleted() + "'" +
            ", achievedPercentage='" + getAchievedPercentage() + "'" +
            "}";
    }

}