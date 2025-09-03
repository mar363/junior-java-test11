package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.InsPolicyDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policies")
public class InsPolicyController {
    private final InsurancePolicyRepository policyRepository;
    private final CarRepository carRepository;


    public InsPolicyController(InsurancePolicyRepository policyRepository, CarRepository carRepository) {
        this.policyRepository = policyRepository;
        this.carRepository = carRepository;
    }

    @PostMapping
    public ResponseEntity<InsurancePolicy> create(@Valid @RequestBody InsPolicyDTO dto) {
        Car car = carRepository.findById(dto.car_id())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        InsurancePolicy policy = new InsurancePolicy();
        policy.setStartDate(dto.startDate());
        policy.setEndDate(dto.endDate());
        policy.setProvider(dto.provider());
        policy.setCar(car);

        return ResponseEntity.status(HttpStatus.CREATED).body(policyRepository.save(policy));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsurancePolicy> update(
            @PathVariable Long id,
            @Valid @RequestBody InsPolicyDTO dto) {


        InsurancePolicy existing = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        Car car = carRepository.findById(dto.car_id())
                .orElseThrow(()->new RuntimeException("Car not found"));

        existing.setStartDate(dto.startDate());
        existing.setEndDate(dto.endDate());
        existing.setProvider(dto.provider());
        existing.setCar(car);
        return ResponseEntity.ok(policyRepository.save(existing));
    }
}