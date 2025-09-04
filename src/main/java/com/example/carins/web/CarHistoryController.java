package com.example.carins.web;

import com.example.carins.model.EventType;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.ClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.CarHistoryDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarHistoryController {

    private final CarRepository carRepo;
    private final ClaimRepository claimRepo;
    private final InsurancePolicyRepository insurancePolicyRepo;


    public CarHistoryController(CarRepository carRepo, ClaimRepository claimRepo, InsurancePolicyRepository insurancePolicyRepo) {
        this.carRepo = carRepo;
        this.claimRepo = claimRepo;
        this.insurancePolicyRepo = insurancePolicyRepo;
    }

    @GetMapping("/{carId}/history")
    public ResponseEntity<List<CarHistoryDto>> getHistory(@PathVariable Long carId) {
        if(!carRepo.existsById(carId)) {
throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found");
        }

        List<CarHistoryDto> claims = claimRepo.findByCarId(carId)
                .stream()
                .map(c-> new CarHistoryDto(
                        EventType.CLAIM,
                        c.getClaimDate(),
                        c.getId(),
                        c.getDescription(),
                        c.getAmount(),
                        null,
                        null,
                        null
                )).toList();

        List<CarHistoryDto> policies = insurancePolicyRepo.findByCarId(carId)
                .stream()
                .map(p -> new CarHistoryDto(
                        EventType.POLICY,
                        p.getStartDate(),
                        p.getId(),
                        null,
                        null,
                        p.getProvider(),
                        p.getStartDate(),
                        p.getEndDate()
                )).toList();

        List<CarHistoryDto> merged = new ArrayList<>();
        merged.addAll(claims);
        merged.addAll(policies);

        merged.sort(Comparator.comparing(CarHistoryDto::date));

        return ResponseEntity.ok(merged);
    }
}
