package com.example.carins.scheduler;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class PolicyExpiryScheduler {
    private static final Logger log = LoggerFactory.getLogger(PolicyExpiryScheduler.class);

    private final InsurancePolicyRepository insurancePolicyRepo;

    public PolicyExpiryScheduler(InsurancePolicyRepository insurancePolicyRepo) {
        this.insurancePolicyRepo = insurancePolicyRepo;
    }

    @Scheduled(cron="0 * 5/ * * * *")
    public void logExpirations() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if(now.isBefore(LocalTime.of(1,0))) {
            List<InsurancePolicy> toLog = insurancePolicyRepo.findByEndDate(today);

            for (InsurancePolicy p : toLog) {
                Long carId = (p.getCar() != null? p.getCar().getId() : null);
                log.info("Policy {} for car {} expired on {}", p.getId(), carId, p.getEndDate());

                p.setExpiryLogged(true);
            }

            if(!toLog.isEmpty()) {
                insurancePolicyRepo.saveAll(toLog);
            }
        }
    }
}
