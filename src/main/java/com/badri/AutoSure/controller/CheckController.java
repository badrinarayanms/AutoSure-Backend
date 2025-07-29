package com.badri.AutoSure.controller;


import com.badri.AutoSure.model.ComplianceResponseDTO;
import com.badri.AutoSure.model.ComplianceResult;
import com.badri.AutoSure.model.ComplianceSummaryDTO;
import com.badri.AutoSure.repository.ComplianceResultRepository;
import com.badri.AutoSure.service.GeminiService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/compliance")
@RequiredArgsConstructor
public class CheckController {

    @Autowired
    private  ComplianceResultRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private GeminiService geminiService;
    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkCompliance(@RequestBody Map<String, Object> carData) {
        try {
            Map<String, Object> result = geminiService.checkCompliance(carData);
            ComplianceResult entity = new ComplianceResult();
            entity.setCarId((String) carData.getOrDefault("carId", "UNKNOWN"));
            entity.setStatus((String) result.get("status"));
            entity.setReason((String) result.get("reason"));
            entity.setCheckedDate(LocalDateTime.now());

            // 🔹 Convert regData and carData back to string JSON
            entity.setCarJson(objectMapper.writeValueAsString(result.get("carData")));
            entity.setRegJson(objectMapper.writeValueAsString(result.get("regData")));

            // 🔹 Handle issues
            List<Map<String, Object>> issueList = (List<Map<String, Object>>) result.get("issues");
            if (issueList != null) {
                List<String> issueStrings = issueList.stream().map(Object::toString).toList();
                entity.setIssues(issueStrings);
            } else {
                entity.setIssues(Collections.emptyList());
            }

            // 🔹 Save to DB
            repository.save(entity);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Failed to process Gemini response",
                    "details", e.getMessage()
            ));
        }
    }

    @GetMapping("/history")
    public List<ComplianceSummaryDTO> getAllHistory() {
        List<ComplianceResult> results = repository.findAll();

        return results.stream().map(res -> {
            ComplianceSummaryDTO dto = new ComplianceSummaryDTO();
            dto.setId(res.getId());
            dto.setCarId(res.getCarId());
            dto.setStatus(res.getStatus());
            dto.setReason(res.getReason());
            dto.setCheckedDate(res.getCheckedDate());
            return dto;
        }).toList();
    }

    @GetMapping("/{Id}")
    public ResponseEntity<ComplianceResponseDTO> getByCarId(@PathVariable long Id) {
        Optional<ComplianceResult> optional = repository.findById(Id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ComplianceResult result = optional.get();

        try {
            ObjectMapper mapper = new ObjectMapper();

            ComplianceResponseDTO dto = new ComplianceResponseDTO();

            dto.setCarId(result.getCarId());
            dto.setStatus(result.getStatus());
            dto.setReason(result.getReason());
            dto.setCheckedDate(result.getCheckedDate());
            dto.setCarData(mapper.readValue(result.getCarJson(), new TypeReference<>() {}));
            dto.setRegData(mapper.readValue(result.getRegJson(), new TypeReference<>() {}));
            dto.setIssues(result.getIssues());

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }




}
