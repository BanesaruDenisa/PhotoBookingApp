package com.example.photobooking.services;

import com.example.photobooking.model.entities.Booking;
import com.example.photobooking.model.entities.DTO.BookingDTO;
import com.example.photobooking.model.entities.Location;
import com.example.photobooking.model.entities.Packages;
import com.example.photobooking.model.entities.User;
import com.example.photobooking.repositories.BookingRepository;
import com.example.photobooking.repositories.LocationRepository;
import com.example.photobooking.repositories.PackageRepository;
import com.example.photobooking.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTests {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PackageRepository packagesRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("""
            Given a valid BookingDTO
            When createBooking is called
            Then a booking is created successfully
            """)
    void test1() {
        
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setDate(LocalDate.now());
        bookingDTO.setStatus("pending");
        bookingDTO.setLocationName("Test Location");
        bookingDTO.setUserId(1L);
        bookingDTO.setPackageId(2L);

        Location location = new Location();
        location.setId(1L);
        location.setName("Test Location");

        User user = new User();
        user.setId(1L);

        Packages packages = new Packages();
        packages.setId(2L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setDate(LocalDate.now());
        booking.setStatus("pending");
        booking.setLocation(location);
        booking.setUser(user);
        booking.setPackages(packages);

        when(locationRepository.findByName("Test Location")).thenReturn(Optional.of(location));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(packagesRepository.findById(2L)).thenReturn(Optional.of(packages));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        
        BookingDTO result = bookingService.createBooking(bookingDTO);
        
        assertNotNull(result);
        assertEquals("Test Location", result.getLocationName());
        assertEquals("pending", result.getStatus());
        assertEquals(1L, result.getUserId());
        assertEquals(2L, result.getPackageId());

        verify(locationRepository, times(1)).findByName("Test Location");
        verify(userRepository, times(1)).findById(1L);
        verify(packagesRepository, times(1)).findById(2L);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    @DisplayName("""
            Given a BookingDTO with missing location name
            When createBooking is called
            Then an exception is thrown
            """)
    void test2() {
        
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setDate(LocalDate.now());
        bookingDTO.setStatus("pending");
        bookingDTO.setUserId(1L);
        bookingDTO.setPackageId(2L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(bookingDTO));

        assertEquals("Location name must be provided.", exception.getMessage());
    }

    @Test
    @DisplayName("""
            Given there are bookings in the database
            When getAllBookings is called
            Then all bookings are returned
            """)
    void test3() {
        
        Location location = new Location();
        location.setId(1L);
        location.setName("Test Location");

        User user = new User();
        user.setId(1L);

        Packages packages = new Packages();
        packages.setId(2L);

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setDate(LocalDate.now());
        booking1.setStatus("confirmed");
        booking1.setLocation(location);
        booking1.setUser(user);
        booking1.setPackages(packages);

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setDate(LocalDate.now().plusDays(1));
        booking2.setStatus("pending");
        booking2.setLocation(location);
        booking2.setUser(user);
        booking2.setPackages(packages);

        List<Booking> bookings = List.of(booking1, booking2);

        when(bookingRepository.findAll()).thenReturn(bookings);

        List<BookingDTO> result = bookingService.getAllBookings();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("confirmed", result.get(0).getStatus());
        assertEquals("pending", result.get(1).getStatus());

        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("""
            Given a booking exists for the given ID
            When getBookingById is called
            Then the booking details are returned
            """)
    void test4() {
        
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setDate(LocalDate.now());
        booking.setStatus("confirmed");

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        
        Booking result = bookingService.getBookingById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("confirmed", result.getStatus());

        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("""
            Given no booking exists for the given ID
            When getBookingById is called
            Then null is returned
            """)
    void test5() {
        
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        Booking result = bookingService.getBookingById(1L);
        
        assertNull(result);
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("""
            Given a valid booking ID and update details
            When updateBooking is called
            Then the booking is updated successfully
            """)
    void test6() {
        
        Location location = new Location();
        location.setId(1L);
        location.setName("Updated Location");

        User user = new User();
        user.setId(1L);

        Packages packages = new Packages();
        packages.setId(1L);

        Booking existingBooking = new Booking();
        existingBooking.setId(1L);
        existingBooking.setDate(LocalDate.now());
        existingBooking.setStatus("pending");
        existingBooking.setLocation(location);
        existingBooking.setUser(user);
        existingBooking.setPackages(packages);

        BookingDTO updatedDTO = new BookingDTO();
        updatedDTO.setLocationName("Updated Location");
        updatedDTO.setDate(LocalDate.now().plusDays(1));
        updatedDTO.setStatus("confirmed");
        updatedDTO.setUserId(1L);
        updatedDTO.setPackageId(1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(existingBooking));
        when(locationRepository.findByName("Updated Location")).thenReturn(Optional.of(location));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(packagesRepository.findById(1L)).thenReturn(Optional.of(packages));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        BookingDTO result = bookingService.updateBooking(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("confirmed", result.getStatus());
        assertEquals(LocalDate.now().plusDays(1), result.getDate());
        assertEquals("Updated Location", result.getLocationName());
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getPackageId());

        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(existingBooking);
        verify(userRepository, times(1)).findById(1L);
        verify(packagesRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("""
            Given a valid booking ID
            When deleteBooking is called
            Then the booking is deleted successfully
            """)
    void test7() {
        
        Booking booking = new Booking();
        booking.setId(1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.deleteBooking(1L);
        
        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).delete(booking);
    }

    @Test
    @DisplayName("""
            Given no booking exists for the given ID
            When deleteBooking is called
            Then an exception is thrown
            """)
    void test8() {
        
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookingService.deleteBooking(1L)
        );

        assertEquals("Booking not found", exception.getMessage());
        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, never()).delete(any());
    }


    @Test
    @DisplayName("""
    Given a valid booking that belongs to the current user
    and not yet confirmed or past date
    When cancelBooking is called
    Then the booking is cancelled successfully
    """)
    void test9() {
        Long bookingId = 10L;
        Long currentUserId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStatus("pending");
        booking.setDate(LocalDate.now().plusDays(2));
        User user = new User();
        user.setId(currentUserId);
        booking.setUser(user);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.cancelBooking(bookingId, currentUserId);

        assertNotNull(result);
        assertEquals("cancelled", result.getStatus());
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).save(booking);
    }

    @Test
    @DisplayName("""
    Given the booking is not found
    When cancelBooking is called
    Then an exception is thrown
    """)
    void test10() {
        Long bookingId = 999L;
        Long currentUserId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.cancelBooking(bookingId, currentUserId));
        assertEquals("Booking not found", exception.getMessage());

        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("""
    Given a booking that does not belong to the current user
    When cancelBooking is called
    Then an exception is thrown (You can't cancel someone else's booking!)
    """)
    void test11() {
        Long bookingId = 10L;
        Long currentUserId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStatus("pending");
        booking.setDate(LocalDate.now().plusDays(3));
        User differentUser = new User();
        differentUser.setId(2L); // alt user
        booking.setUser(differentUser);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.cancelBooking(bookingId, currentUserId));
        assertEquals("You can't cancel someone else's booking!", exception.getMessage());

        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("""
    Given a booking that is already confirmed or in the past
    When cancelBooking is called
    Then 'Too late to cancel this booking!' exception is thrown
    """)
    void test12() {
        Long bookingId = 10L;
        Long currentUserId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStatus("confirmed");
        booking.setDate(LocalDate.now().minusDays(1)); // deja in trecut
        User user = new User();
        user.setId(currentUserId);
        booking.setUser(user);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.cancelBooking(bookingId, currentUserId));
        assertEquals("Too late to cancel this booking!", exception.getMessage());

        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).save(any());
    }

}
