package com.example.photobooking.services;

import com.example.photobooking.model.entities.Location;
import com.example.photobooking.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocation(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public Location findByName(String name) {
        Optional<Location> locOpt = locationRepository.findByName(name);
        return locOpt.orElse(null);
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location createLocationIfNotExists(String name) {

        Location existing = findByName(name);
        if (existing != null) {
            return existing;
        }

        Location newLoc = new Location();
        newLoc.setName(name);
        newLoc.setDescription("Auto-created location");
        return locationRepository.save(newLoc);
    }

    public Location updateLocation(Long id, Location updated) {
        return locationRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            return locationRepository.save(existing);
        }).orElse(null);
    }

    public boolean deleteLocation(Long id) {
        if(locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
