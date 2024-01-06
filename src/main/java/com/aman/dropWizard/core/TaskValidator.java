package com.aman.dropWizard.core;

import javax.ws.rs.BadRequestException;
import java.time.LocalDate;

public class TaskValidator {
    public void validateTask(Task task) {
        validateTaskNotNull(task);
        validateDescription(task.getDescription());
        validateDate(task.getStartDate(), "Task Start Date");
        validateDate(task.getTargetDate(), "Task Target Date");
    }

    private void validateTaskNotNull(Task task) {
        if (task == null) {
            throw new BadRequestException("No Task provided");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new BadRequestException("Task Description is required");
        }
    }

    private void validateDate(LocalDate date, String fieldName) {
        if (date == null || date.toString().isEmpty()) {
            throw new BadRequestException(fieldName + " is required");
        }
    }
}
