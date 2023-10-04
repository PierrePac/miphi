package fr.mipih.rh.testcandidats.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledEvent {

    private final EntretienService entretienService;

    @Scheduled(fixedRate = 86400000)
    public void deleteExpiredEntretien() {
        entretienService.deleteExpiredEntretien();
    }
}
