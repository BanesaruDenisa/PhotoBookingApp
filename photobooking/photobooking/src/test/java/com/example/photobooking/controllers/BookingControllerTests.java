package com.example.photobooking.controllers;

import com.example.photobooking.model.entities.Booking;
import com.example.photobooking.model.entities.DTO.BookingDTO;
import com.example.photobooking.services.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTests {

    @Mock
    private BookingService bookingService;

    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        bookingController = new BookingController();
        ReflectionTestUtils.setField(bookingController, "bookingService", bookingService);
    }

    @Test
    @DisplayName("""
            Given the system returns an empty list
            When getAllBookings is called
            Then a 200 OK is returned with an empty list
            """)
    void test1() {
        when(bookingService.getAllBookings()).thenReturn(Collections.emptyList());
        ResponseEntity<List<BookingDTO>> response = bookingController.getAllBookings();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(bookingService).getAllBookings();
    }

    @Test
    @DisplayName("""
            Given a booking with ID=10 exists
            When getBookingById is called
            Then the booking is returned with 200
            """)
    void test2() {
        Booking mockBooking = new Booking();
        mockBooking.setId(10L);
        when(bookingService.getBookingById(10L)).thenReturn(mockBooking);
        BookingDTO mockDTO = new BookingDTO(10L, "LocationX", LocalDate.now(), "pending", 1L, 2L);
        when(bookingService.convertToDTO(mockBooking)).thenReturn(mockDTO);
        ResponseEntity<?> response = bookingController.getBookingById(10L);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof BookingDTO);
        BookingDTO dto = (BookingDTO) response.getBody();
        assertEquals(10L, dto.getId());
        verify(bookingService).getBookingById(10L);
        verify(bookingService).convertToDTO(mockBooking);
    }

    @Test
    @DisplayName("""
            Given no booking for ID=999
            When getBookingById is called
            Then returns 404 with 'Booking not found'
            """)
    void test3() {
        when(bookingService.getBookingById(999L)).thenReturn(null);
        ResponseEntity<?> response = bookingController.getBookingById(999L);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Booking not found", response.getBody());
        verify(bookingService).getBookingById(999L);
        verify(bookingService, never()).convertToDTO(any());
    }

    @Test
    @DisplayName("""
            Given a valid BookingDTO
            When createBooking is called
            Then a booking is created successfully
            """)
    void test4() {
        BookingDTO requestDto = new BookingDTO(null, "Paris", LocalDate.now(), "pending", 1L, 2L);
        BookingDTO responseDto = new BookingDTO(100L, "Paris", LocalDate.now(), "pending", 1L, 2L);
        when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(responseDto);
        ResponseEntity<BookingDTO> response = bookingController.createBooking(requestDto);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(100L, response.getBody().getId());
        verify(bookingService).createBooking(requestDto);
    }

    @Test
    @DisplayName("""
            Given a valid update for booking with ID=10
            When updateBooking is called
            Then returns 200 and the updated booking
            """)
    void test5() {
        BookingDTO updateDto = new BookingDTO(null, "ParisUpdated", LocalDate.now(), "confirmed", 3L, 4L);
        BookingDTO updatedResponse = new BookingDTO(10L, "ParisUpdated", LocalDate.now(), "confirmed", 3L, 4L);
        when(bookingService.updateBooking(eq(10L), any(BookingDTO.class))).thenReturn(updatedResponse);
        ResponseEntity<?> response = bookingController.updateBooking(10L, updateDto);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof BookingDTO);
        BookingDTO body = (BookingDTO) response.getBody();
        assertEquals(10L, body.getId());
        assertEquals("ParisUpdated", body.getLocationName());
        verify(bookingService).updateBooking(eq(10L), eq(updateDto));
    }

    @Test
    @DisplayName("""
            Given no booking for ID=999
            When updateBooking is called
            Then returns 404 with 'Booking not found'
            """)
    void test6() {
        BookingDTO updateDto = new BookingDTO(null, "NoWhere", LocalDate.now(), "pending", 1L, 2L);
        when(bookingService.updateBooking(eq(999L), any(BookingDTO.class)))
                .thenThrow(new RuntimeException("Booking not found"));
        ResponseEntity<?> response = bookingController.updateBooking(999L, updateDto);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Booking not found", response.getBody());
        verify(bookingService).updateBooking(eq(999L), eq(updateDto));
    }

    @Test
    @DisplayName("""
            Given a booking with ID=123
            When deleteBooking is called
            Then returns 204 no content
            """)
    void test7() {
        doNothing().when(bookingService).deleteBooking(123L);
        ResponseEntity<Void> response = bookingController.deleteBooking(123L);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(bookingService).deleteBooking(123L);
    }

    @Test
    @DisplayName("""
    Given a valid booking ID and the user is photographer
    When confirmBooking endpoint is called
    Then return 200 OK with success message
    """)
    void testConfirmBooking_Success() {
        Long bookingId = 10L;
        Booking booking = new Booking();
        booking.setId(bookingId);

        when(bookingService.confirmBooking(bookingId)).thenReturn(booking);

        ResponseEntity<?> response = bookingController.confirmBooking(bookingId);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof String);
        String msg = (String) response.getBody();
        assertTrue(msg.contains("confirmed successfully!"));

        verify(bookingService).confirmBooking(bookingId);
    }

    @Test
    @DisplayName("""
    Given confirmBooking throws 'Booking not found'
    When confirmBooking endpoint is called
    Then return 404 with that message
    """)
    void testConfirmBooking_BookingNotFound() {
        Long bookingId = 999L;
        when(bookingService.confirmBooking(bookingId))
                .thenThrow(new RuntimeException("Booking not found"));

        ResponseEntity<?> response = bookingController.confirmBooking(bookingId);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Booking not found", response.getBody());

        verify(bookingService).confirmBooking(bookingId);
    }

    @Test
    @DisplayName("""
    Given confirmBooking throws 'You do not have permission to confirm this booking!'
    When confirmBooking endpoint is called
    Then return 403 with that message
    """)
    void testConfirmBooking_NoPermission() {
        Long bookingId = 20L;
        when(bookingService.confirmBooking(bookingId))
                .thenThrow(new RuntimeException("You do not have permission to confirm this booking!"));

        ResponseEntity<?> response = bookingController.confirmBooking(bookingId);
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("You do not have permission to confirm this booking!", response.getBody());

        verify(bookingService).confirmBooking(bookingId);
    }


    @Test
    @DisplayName("""
    Given a valid booking ID and user is owner
    When cancelBooking is called
    Then booking is cancelled (200 OK)
    """)
    void test8() {
        Long bookingId = 10L;

        Booking mockBooking = new Booking();
        mockBooking.setId(bookingId);
        mockBooking.setStatus("cancelled");

        when(bookingService.cancelBooking(eq(bookingId), anyLong())).thenReturn(mockBooking);

        ResponseEntity<?> response = bookingController.cancelBooking(bookingId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Booking cancelled successfully", response.getBody());

        verify(bookingService).cancelBooking(eq(bookingId), anyLong());
    }

    @Test
    @DisplayName("""
    Given the booking does not exist
    When cancelBooking is called
    Then returns 404 and 'Booking not found'
    """)
    void test9() {
        Long bookingId = 999L;
        when(bookingService.cancelBooking(eq(999L), anyLong()))
                .thenThrow(new RuntimeException("Booking not found"));

        ResponseEntity<?> response = bookingController.cancelBooking(999L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Booking not found", response.getBody());
        verify(bookingService).cancelBooking(eq(999L), anyLong());
    }

    @Test
    @DisplayName("""
    Given the booking is too late or belongs to another user
    When cancelBooking is called
    Then returns 403 with the exception message
    """)
    void test10() {
        Long bookingId = 15L;
        when(bookingService.cancelBooking(eq(15L), anyLong()))
                .thenThrow(new RuntimeException("Too late to cancel this booking!"));

        ResponseEntity<?> response = bookingController.cancelBooking(15L);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Too late to cancel this booking!", response.getBody());
        verify(bookingService).cancelBooking(eq(15L), anyLong());
    }

}
