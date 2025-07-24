package com.api.timesheet.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "timecard")
@Data
public class TimeCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Start date is mandatory")
    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    @NotBlank(message = "End date is mandatory")
    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;

    @Min(value = 0, message = "Time cannot be negative")
    @Column(name = "monday_time", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int mondayTime;

    @Min(value = 0, message = "Time cannot be negative")
    @Column(name = "tuesday_time", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int tuesdayTime;

    @Min(value = 0, message = "Time cannot be negative")
    @Column(name = "wednesday_time", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int wednesdayTime;

    @Min(value = 0, message = "Time cannot be negative")
    @Column(name = "thursday_time", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int thursdayTime;

    @Min(value = 0, message = "Time cannot be negative")
    @Column(name = "friday_time", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int fridayTime;

    @Min(value = 0, message = "Time cannot be negative")
    @Column(name = "total_time", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int totalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projects_id")
    private Projects projects;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;

}
