package com.example.photobooking.controllers;

import com.example.photobooking.model.entities.Packages;
import com.example.photobooking.services.PackagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/packages")
@Tag(name = "Packages Controller", description = "Endpoints pentru gestionarea pachetelor (serviciilor foto).")
public class PackagesController {

    @Autowired
    private PackagesService packageService;

    @GetMapping
    @Operation(
            summary = "Get all packs",
            description = "Retrieve all photography packs from the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of packs retrieved successfully")
            }
    )
    public List<Packages> getAllPacks() {
        return packageService.getAllPacks();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get pack by ID",
            description = "Retrieve details of a specific pack by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pack retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Pack not found")
            }
    )
    public Packages getPackById(@PathVariable Long id) {
        return packageService.getPackById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Create a new pack",
            description = "Add a new photography package to the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pack created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<?> createPack(@Valid @RequestBody Packages aPackages) {
        try {
            Packages savedPackages = packageService.createPack(aPackages);
            return ResponseEntity.ok(savedPackages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing pack",
            description = "Update the details of a photography package by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pack updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Pack not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public Packages updateServicePackage(@PathVariable Long id, @RequestBody @Valid Packages updatedPackages) {
        return packageService.updateServicePackage(id, updatedPackages);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a pack",
            description = "Remove a photography package by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Pack deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Pack not found")
            }
    )
    public void deleteServicePackage(@PathVariable Long id) {
        packageService.deleteServicePackage(id);
    }
}
