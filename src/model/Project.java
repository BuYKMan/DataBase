package model;

import Entity.Game;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Project extends Game {
    private int projectId;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private BigDecimal budget;
    private int managerId;
    private int gameId;

    // Getters and setters

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Project(int projectId, String projectName, LocalDate startDate, LocalDate endDate, String status, BigDecimal budget, int managerId, int gameId) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.budget = budget;
        this.managerId = managerId;
        this.gameId = gameId;
    }

    public Project() {
    }
    // Constructor
}