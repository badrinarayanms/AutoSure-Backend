package com.badri.AutoSure.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceResponseDTO {
    private String carId;
    private String status;
    private String reason;
    private LocalDateTime checkedDate;
    private Map<String, Object> regData;
    private Map<String, Object> carData;
    private List<String> issues;

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

    public Map<String, Object> getRegData() {
        return regData;
    }

    public void setRegData(Map<String, Object> regData) {
        this.regData = regData;
    }

    public Map<String, Object> getCarData() {
        return carData;
    }

    public void setCarData(Map<String, Object> carData) {
        this.carData = carData;
    }

    public List<String> getIssues() {
        return issues;
    }

    public void setIssues(List<String> issues) {
        this.issues = issues;
    }

    public ComplianceResponseDTO(String carId, String status, String reason, LocalDateTime checkedDate, Map<String, Object> regData, Map<String, Object> carData, List<String> issues) {
        this.carId = carId;
        this.status = status;
        this.reason = reason;
        this.checkedDate = checkedDate;
        this.regData = regData;
        this.carData = carData;
        this.issues = issues;
    }

    public ComplianceResponseDTO() {

    }
}

