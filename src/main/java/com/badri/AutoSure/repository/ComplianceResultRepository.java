package com.badri.AutoSure.repository;

import com.badri.AutoSure.model.ComplianceResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComplianceResultRepository extends JpaRepository<ComplianceResult, Long> {
    List<ComplianceResult> findByCarId(String carId);
    Optional<ComplianceResult> findTopByCarIdOrderByCheckedDateDesc(String carId);


}
