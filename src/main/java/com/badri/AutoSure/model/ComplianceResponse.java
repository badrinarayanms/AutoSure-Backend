package com.badri.AutoSure.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ComplianceResponse {
    private String status;
    private Map<String, Object> regData;
    private Map<String, Object> carData;
    private List<Issue> issues;
    private String reason;
    private String carId;
    private LocalDateTime checkedDate;
}

