package com.badri.AutoSure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ComplianceResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String carId;
    private String status; // approved / rejected
    private String reason;
    private LocalDateTime checkedDate;

    @ElementCollection
    private List<String> issues;

    @Lob
    private String regJson;

    @Lob
    private String carJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(LocalDateTime checkedDate) {
        this.checkedDate = checkedDate;
    }

    public List<String> getIssues() {
        return issues;
    }

    public void setIssues(List<String> issues) {
        this.issues = issues;
    }


    public String getRegJson() {
        return regJson;
    }

    public void setRegJson(String regJson) {
        this.regJson = regJson;
    }

    public String getCarJson() {
        return carJson;
    }

    public void setCarJson(String carJson) {
        this.carJson = carJson;
    }
}

