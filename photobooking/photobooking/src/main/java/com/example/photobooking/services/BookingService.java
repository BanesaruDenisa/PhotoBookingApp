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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packagesRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private NotificationService notificationService;


    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }


    public BookingDTO createBooking(BookingDTO bookingDTO) {

        if (bookingDTO.getLocationName() == null || bookingDTO.getLocationName().isEmpty()) {
            throw new IllegalArgumentException("Location name must be provided.");
        }


        Location location = locationRepository.findByName(bookingDTO.getLocationName())
                .orElseGet(() -> {
                    Location newLocation = new Location();
                    newLocation.setName(bookingDTO.getLocationName());
                    return locationRepository.save(newLocation);
                });


        Booking booking = new Booking();
        booking.setDate(bookingDTO.getDate());
        booking.setStatus(bookingDTO.getStatus());
        booking.setLocation(location);
        booking.setUser(userRepository.findById(bookingDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
        booking.setPackages(packagesRepository.findById(bookingDTO.getPackageId())
                .orElseThrow(() -> new IllegalArgumentException("Package not found")));


        Booking savedBooking = bookingRepository.save(booking);

        return new BookingDTO(
                savedBooking.getId(),
                savedBooking.getLocation().getName(),
                savedBooking.getDate(),
                savedBooking.getStatus(),
                savedBooking.getUser().getId(),
                savedBooking.getPackages().getId()
        );
    }



    public Booking createBookingFromDTO(User user, Packages Package, BookingDTO dto) {
        Location location = locationService.createLocationIfNotExists(dto.getLocationName());

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setPackages(Package);
        booking.setLocation(location);
        booking.setDate(dto.getDate());
        booking.setStatus("pending");


        return bookingRepository.save(booking);
    }

    public BookingDTO updateBooking(Long bookingId, BookingDTO updatedDTO) {

        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (updatedDTO.getLocationName() != null && !updatedDTO.getLocationName().isEmpty()) {
            Location location = locationRepository.findByName(updatedDTO.getLocationName())
                    .orElseGet(() -> {
                        Location newLoc = new Location();
                        newLoc.setName(updatedDTO.getLocationName());
                        return locationRepository.save(newLoc);
                    });
            existingBooking.setLocation(location);
        }

        if (updatedDTO.getDate() != null) {
            existingBooking.setDate(updatedDTO.getDate());
        }

        if (updatedDTO.getStatus() != null && !updatedDTO.getStatus().isEmpty()) {
            existingBooking.setStatus(updatedDTO.getStatus());
        }

        if (updatedDTO.getUserId() != null) {
            User user = userRepository.findById(updatedDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            existingBooking.setUser(user);
        }

        if (updatedDTO.getPackageId() != null) {
            Packages pack = packagesRepository.findById(updatedDTO.getPackageId())
                    .orElseThrow(() -> new IllegalArgumentException("Package not found"));
            existingBooking.setPackages(pack);
        }

        Booking savedBooking = bookingRepository.save(existingBooking);

        return convertToDTO(savedBooking);
    }

    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        bookingRepository.delete(booking);
    }

    public BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setDate(booking.getDate());
        dto.setStatus(booking.getStatus());

        if (booking.getUser() != null) {
            dto.setUserId(booking.getUser().getId());
        } else {
            dto.setUserId(null);
        }

        if (booking.getPackages() != null) {
            dto.setPackageId(booking.getPackages().getId());
        } else {
            dto.setPackageId(null);
        }

        if (booking.getLocation() != null) {
            dto.setLocationName(booking.getLocation().getName());
        } else {
            dto.setLocationName("Unknown");
        }
        return dto;
    }

    public Booking confirmBooking(Long bookingId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Not authenticated");
        }

        String currentEmail = auth.getName();
        User currentUser = userRepository.findByEmail(currentEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found in DB");
        }

        String role = currentUser.getRole();

        if (
                !"photographer".equalsIgnoreCase(role)
                        && !"admin".equalsIgnoreCase(role)
                        && !"ROLE_PHOTOGRAPHER".equalsIgnoreCase(role)
                        && !"ROLE_ADMIN".equalsIgnoreCase(role)
        ) {
            throw new RuntimeException("You do not have permission to confirm this booking!");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus("confirmed");
        Booking saved = bookingRepository.save(booking);

        User client = booking.getUser();
        notificationService.notifyUser(
                "Your booking (ID=" + bookingId + ") has been confirmed!",
                client
        );
        return saved;
    }


    public Booking cancelBooking(Long bookingId, Long currentUserId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {

            throw new RuntimeException("Booking not found");
        }

        if (!booking.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("You can't cancel someone else's booking!");
        }

        if ("confirmed".equalsIgnoreCase(booking.getStatus()) ||
                booking.getDate().isBefore(java.time.LocalDate.now())) {
            throw new RuntimeException("Too late to cancel this booking!");
        }
        booking.setStatus("cancelled");
        return bookingRepository.save(booking);
    }



}
