package com.example.photobooking.services;

import com.example.photobooking.model.entities.Packages;
import com.example.photobooking.repositories.PackageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PackagesServiceTests {

    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private PackagesService packagesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("""
            Given there are packages in the database
            When getAllPacks is called
            Then all packages are returned
            """)
    void test1() {
        
        Packages package1 = new Packages();
        package1.setId(1L);
        package1.setName("Basic");
        package1.setDescription("Basic package");
        package1.setPrice(100.0);

        Packages package2 = new Packages();
        package2.setId(2L);
        package2.setName("Premium");
        package2.setDescription("Premium package");
        package2.setPrice(200.0);

        when(packageRepository.findAll()).thenReturn(List.of(package1, package2));

        List<Packages> result = packagesService.getAllPacks();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Basic", result.get(0).getName());
        assertEquals("Premium", result.get(1).getName());

        verify(packageRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("""
            Given a package exists with a specific ID
            When getPackById is called
            Then the correct package is returned
            """)
    void test2() {
        
        Packages package1 = new Packages();
        package1.setId(1L);
        package1.setName("Basic");
        package1.setDescription("Basic package");
        package1.setPrice(100.0);

        when(packageRepository.findById(1L)).thenReturn(Optional.of(package1));
        
        Packages result = packagesService.getPackById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Basic", result.getName());

        verify(packageRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("""
            Given no package exists with a specific ID
            When getPackById is called
            Then null is returned
            """)
    void test3() {
        
        when(packageRepository.findById(1L)).thenReturn(Optional.empty());

        Packages result = packagesService.getPackById(1L);
        
        assertNull(result);
        verify(packageRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("""
            Given a new package to create
            When createPack is called
            Then the package is saved and returned
            """)
    void test4() {
        
        Packages newPackage = new Packages();
        newPackage.setName("Basic");
        newPackage.setDescription("Basic package");
        newPackage.setPrice(100.0);

        when(packageRepository.save(any(Packages.class))).thenAnswer(invocation -> {
            Packages savedPackage = invocation.getArgument(0);
            savedPackage.setId(1L);
            return savedPackage;
        });

        Packages result = packagesService.createPack(newPackage);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Basic", result.getName());

        verify(packageRepository, times(1)).save(newPackage);
    }

    @Test
    @DisplayName("""
            Given a package with an existing ID
            When createPack is called
            Then an exception is thrown
            """)
    void test5() {
        
        Packages existingPackage = new Packages();
        existingPackage.setId(1L);
        existingPackage.setName("Basic");
        existingPackage.setDescription("Basic package");
        existingPackage.setPrice(100.0);

        when(packageRepository.existsById(1L)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> packagesService.createPack(existingPackage)
        );

        assertEquals("Pack with this ID already exists", exception.getMessage());
    }

    @Test
    @DisplayName("""
            Given an existing package ID and updated details
            When updateServicePackage is called
            Then the package is updated and saved
            """)
    void test6() {
        
        Packages existingPackage = new Packages();
        existingPackage.setId(1L);
        existingPackage.setName("Basic");
        existingPackage.setDescription("Basic package");
        existingPackage.setPrice(100.0);

        Packages updatedPackage = new Packages();
        updatedPackage.setName("Premium");
        updatedPackage.setDescription("Premium package");
        updatedPackage.setPrice(200.0);

        when(packageRepository.findById(1L)).thenReturn(Optional.of(existingPackage));
        when(packageRepository.save(any(Packages.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Packages result = packagesService.updateServicePackage(1L, updatedPackage);
        
        assertNotNull(result);
        assertEquals("Premium", result.getName());
        assertEquals("Premium package", result.getDescription());
        assertEquals(200.0, result.getPrice());

        verify(packageRepository, times(1)).findById(1L);
        verify(packageRepository, times(1)).save(existingPackage);
    }

    @Test
    @DisplayName("""
            Given an existing package ID
            When deleteServicePackage is called
            Then the package is deleted
            """)
    void test7() {
        
        doNothing().when(packageRepository).deleteById(1L);
        
        packagesService.deleteServicePackage(1L);

        verify(packageRepository, times(1)).deleteById(1L);
    }
}
