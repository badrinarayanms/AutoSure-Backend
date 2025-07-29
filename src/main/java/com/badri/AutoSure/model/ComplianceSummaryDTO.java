package com.badri.AutoSure.model;

import java.time.LocalDateTime;

public class ComplianceSummaryDTO {
    private long id;
    private String carId;
    private String status;
    private String reason;
    private LocalDateTime checkedDate;

    public ComplianceSummaryDTO() {

    }
    public ComplianceSummaryDTO(String carId, String status, String reason, LocalDateTime checkedDate) {
        this.carId = carId;
        this.status = status;
        this.reason = reason;
        this.checkedDate = checkedDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}

