package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.model.Claim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.ClaimRepository;
import com.example.carins.web.dto.ClaimDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequestMapping("/api/cars/{carId}/claims")
public class ClaimController {
    private final CarRepository carRepo;
    private final ClaimRepository claimRepo;

    public ClaimController(CarRepository carRepo, ClaimRepository claimRepo) {
        this.carRepo = carRepo;
        this.claimRepo = claimRepo;
    }

    @PostMapping
    public ResponseEntity<ClaimDto> create(@PathVariable Long carId,
                                           @Valid @RequestBody ClaimDto dto) {

        Car car = carRepo.findById(carId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        Claim c = new Claim();
        c.setCar(car);
        c.setClaimDate(dto.claimDate());
        c.setDescription(dto.description());
        c.setAmount(dto.amount());
        Claim saved = claimRepo.save(c);

        ClaimDto response = new ClaimDto(
                saved.getId(),
                carId,
                saved.getClaimDate(),
                saved.getDescription(),
                saved.getAmount()
        );
        URI location = URI.create("/api/cars" + carId + "/claims" + saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
