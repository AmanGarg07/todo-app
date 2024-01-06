package com.aman.dropWizard.core;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@NamedQueries({
        @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t"),
        @NamedQuery(name = "Task.getById", query = "select t from Task t where t.id = :id"),
        @NamedQuery(name = "Task.deleteById",query = "DELETE FROM Task t WHERE t.id = :id")
})
public class Task {

    @Id
    @Column(name = "idtasks")
    private int id;
    @Column(name = "description")
    private String desc;
    @Column(name = "startDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(name = "endDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetDate;
    @Column(name = "status")
    private String status;

    public Task(int id, String desc, LocalDate startDate, LocalDate targetDate, String status) {
        this.id = id;
        this.desc = desc;
        this.startDate = startDate;
        this.targetDate = targetDate;
        this.status = status;
    }

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
