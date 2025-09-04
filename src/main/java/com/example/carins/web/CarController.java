package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.repo.CarRepository;
import com.example.carins.service.CarService;
import com.example.carins.web.dto.CarDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;
    private final CarRepository carRepo;
    public CarController(CarService service, CarRepository carRepo) {
        this.service = service;
        this.carRepo = carRepo;
    }
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final LocalDate MIN_SUPPORTED_DATE = LocalDate.of(1900, 1,1);
    private static final LocalDate MAX_SUPPORTED_DATE = LocalDate.of(2100, 12,31);

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(this::toDto).toList();
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<?> isInsuranceValid(@PathVariable Long carId, @RequestParam (value="date", required=false) String date) {
        // validate date format and handle errors consistently
        if(date == null || date.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Missing required paramete 'date' " +
                    "(expected ISO YYYY-MM-DD)"));
        }

        final LocalDate dateStr;
        try {
            dateStr = LocalDate.parse(date, ISO_DATE);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Invalid 'date' format. Expected ISO YYYY-MM-DD")
            );
        }

        if(dateStr.isBefore(MIN_SUPPORTED_DATE) || dateStr.isAfter(MAX_SUPPORTED_DATE)) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Date out of supported range: " + MIN_SUPPORTED_DATE + "to " + MAX_SUPPORTED_DATE + "!")
            );
        }

        if(!carRepo.existsById(carId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("message", "Car " + carId + " not found")
            );
        }



        boolean valid = service.isInsuranceValid(carId, dateStr);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, dateStr.toString(), valid));
    }

    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null);
    }

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {}
}
