package com.example.photobooking.services;

import com.example.photobooking.model.entities.Statistics;
import com.example.photobooking.repositories.StatisticsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public List<Statistics> getAllStatistics() {
        return statisticsRepository.findAll();
    }

    public Statistics getStatisticsByUserId(Long userId) {
        return statisticsRepository.findByUserId(userId).orElse(null);
    }

    public Statistics createOrUpdateStatistics(Statistics stats) {
        // Caută dacă există deja ceva pentru userul respectiv
        Optional<Statistics> existing = statisticsRepository.findByUserId(stats.getUser().getId());

        if (existing.isPresent()) {
            Statistics existingStats = existing.get();
            // actualizezi valorile
            existingStats.setTotalBookings(stats.getTotalBookings());
            existingStats.setEstimatedEarnings(stats.getEstimatedEarnings());
            return statisticsRepository.save(existingStats);
        } else {
            // nu există, creezi o intrare nouă
            return statisticsRepository.save(stats);
        }
    }

}
