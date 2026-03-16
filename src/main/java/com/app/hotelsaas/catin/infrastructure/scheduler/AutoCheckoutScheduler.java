package com.app.hotelsaas.catin.infrastructure.scheduler;

import com.app.hotelsaas.catin.application.usecase.ocupation.AutoCheckoutUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoCheckoutScheduler {

    private final AutoCheckoutUseCase autoCheckoutUseCase;

    // Corre todos los días a medianoche
    @Scheduled(cron = "0 47 15 * * *", zone = "America/Lima")
    public void runAutoCheckout() {
        log.info("=== AUTO-CHECKOUT iniciado ===");
        int total = autoCheckoutUseCase.execute();
        log.info("=== AUTO-CHECKOUT finalizado — {} ocupaciones cerradas ===", total);
    }
}