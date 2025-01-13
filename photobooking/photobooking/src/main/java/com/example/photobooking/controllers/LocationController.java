package com.example.photobooking.controllers;

import com.example.photobooking.model.entities.Location;
import com.example.photobooking.services.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@Tag(name = "Location Controller", description = "Endpoints for managing locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    @Operation(summary = "Get all locations", description = "Retrieve list of all locations.")
    @ApiResponse(responseCode = "200", description = "OK")
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get location by ID", description = "Retrieve a specific location by its ID.")
    public Location getLocationById(@PathVariable Long id) {
        return locationService.getLocation(id);
    }

    @PostMapping
    @Operation(summary = "Create a new location", description = "Add a new location to the database.")
    public Location createLocation(@Valid @RequestBody Location location) {
        return locationService.createLocation(location);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing location", description = "Update an existing location by ID.")
    public Location updateLocation(@PathVariable Long id, @RequestBody Location updated) {
        return locationService.updateLocation(id, updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a location", description = "Remove a location from the database by ID.")
    public void deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
    }
}
