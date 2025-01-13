package com.example.photobooking.controllers;

import com.example.photobooking.model.entities.Statistics;
import com.example.photobooking.services.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@Tag(name = "Statistics Controller", description = "Endpoints for user statistics (e.g. total bookings, earnings).")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PreAuthorize("hasRole('PHOTOGRAPHER')")
    @GetMapping
    @Operation(
            summary = "Get all statistics",
            description = "Retrieve all statistics entries from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - list of statistics returned")
            }
    )
    public List<Statistics> getAllStatistics() {
        return statisticsService.getAllStatistics();
    }

    @PreAuthorize("hasRole('PHOTOGRAPHER')")
    @GetMapping("/{userId}")
    @Operation(
            summary = "Get statistics by user",
            description = "Retrieve the statistics for a specific user ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Statistics returned"),
                    @ApiResponse(responseCode = "404", description = "No statistics found for this user")
            }
    )
    public Statistics getStatisticsForUser(@PathVariable Long userId) {
        return statisticsService.getStatisticsByUserId(userId);
    }

    @PreAuthorize("hasRole('PHOTOGRAPHER')")
    @PostMapping
    @Operation(
            summary = "Create or update statistics",
            description = "Create new statistics entry or update if it already exists.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Statistics entry created/updated successfully")
            }
    )
    public Statistics createOrUpdateStatistics(@Valid  @RequestBody Statistics stats) {
        return statisticsService.createOrUpdateStatistics(stats);
    }

}
