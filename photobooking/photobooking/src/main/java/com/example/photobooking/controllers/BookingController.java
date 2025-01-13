package com.example.photobooking.controllers;

import com.example.photobooking.model.entities.Booking;
import com.example.photobooking.model.entities.DTO.BookingDTO;
import com.example.photobooking.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Booking Controller", description = "Endpoints pentru gestionarea rezervărilor (Bookings).")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    @Operation(
            summary = "Get all bookings",
            description = "Retrieve all bookings from the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of bookings retrieved successfully")
            }
    )
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get booking by ID",
            description = "Retrieve a specific booking by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Booking not found")
            }
    )

    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
        }
        return ResponseEntity.ok(bookingService.convertToDTO(booking));
    }


    @PostMapping
    @Operation(
            summary = "Create a booking",
            description = "Create a new booking (reservation) for a user and a specific package.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking created successfully"),
                    @ApiResponse(responseCode = "401", description = "User not authenticated"),
                    @ApiResponse(responseCode = "400", description = "Invalid Package ID or other data issues")
            }
    )


    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);
        return ResponseEntity.ok(createdBooking);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update booking by ID",
            description = "Update an existing booking in the system by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Booking not found")
            }
    )
    public ResponseEntity<?> updateBooking(
            @PathVariable Long id,
            @RequestBody @Valid BookingDTO bookingDTO) {

        try {
            BookingDTO updated = bookingService.updateBooking(id, bookingDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a booking",
            description = "Remove a specific booking from the system by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Booking deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Booking not found")
            }
    )
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmBooking(@PathVariable Long id) {
        try {
            Booking confirmed = bookingService.confirmBooking(id);
            return ResponseEntity.ok("Booking (ID=" + confirmed.getId() + ") confirmed successfully!");
        } catch (RuntimeException e) {
            String msg = e.getMessage();

            if ("Booking not found".equals(msg)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
            }

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        Long currentUserId = 123L;

        try {
            Booking cancelledBooking = bookingService.cancelBooking(id, currentUserId);

            return ResponseEntity.ok("Booking cancelled successfully");
        } catch (RuntimeException e) {

            String message = e.getMessage();
            if ("Booking not found".equalsIgnoreCase(message)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
        }
    }
}
