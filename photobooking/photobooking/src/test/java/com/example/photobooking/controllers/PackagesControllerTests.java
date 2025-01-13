package com.example.photobooking.controllers;

import com.example.photobooking.model.entities.Packages;
import com.example.photobooking.services.PackagesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PackagesControllerTest {

    @Mock
    private PackagesService packageService;

    private PackagesController packagesController;

    @BeforeEach
    void setUp() {
        packagesController = new PackagesController();
        ReflectionTestUtils.setField(packagesController, "packageService", packageService);
    }

    @Test
    @DisplayName("""
            Given the system returns an empty list
            When getAllPacks is called
            Then a list is returned with no elements
            """)
    void testGetAllPacks_Empty() {
        when(packageService.getAllPacks()).thenReturn(Collections.emptyList());
        List<Packages> result = packagesController.getAllPacks();
        assertTrue(result.isEmpty());
        verify(packageService).getAllPacks();
    }

    @Test
    @DisplayName("""
            Given a valid Pack ID
            When getPackById is called
            Then the pack is returned
            """)
    void testGetPackById_Found() {
        Packages mockPack = new Packages();
        mockPack.setId(100L);
        mockPack.setName("Gold Package");
        when(packageService.getPackById(100L)).thenReturn(mockPack);

        Packages result = packagesController.getPackById(100L);
        assertNotNull(result);
        assertEquals("Gold Package", result.getName());
        verify(packageService).getPackById(100L);
    }

    @Test
    @DisplayName("""
            Given a pack ID that does not exist
            When getPackById is called
            Then return null
            """)
    void testGetPackById_NotFound() {
        when(packageService.getPackById(999L)).thenReturn(null);
        Packages result = packagesController.getPackById(999L);
        assertNull(result);
        verify(packageService).getPackById(999L);
    }

    @Test
    @DisplayName("""
            Given a valid pack object
            When createPack is called
            Then the pack is created successfully
            """)
    void testCreatePack_Success() {
        Packages mockPack = new Packages();
        mockPack.setName("Wedding Package");

        Packages savedPack = new Packages();
        savedPack.setId(10L);
        savedPack.setName("Wedding Package");

        when(packageService.createPack(any(Packages.class))).thenReturn(savedPack);

        ResponseEntity<?> response = packagesController.createPack(mockPack);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Packages);

        Packages body = (Packages) response.getBody();
        assertEquals(10L, body.getId());
        assertEquals("Wedding Package", body.getName());
        verify(packageService).createPack(mockPack);
    }

    @Test
    @DisplayName("""
            Given an exception occurs when creating a pack
            When createPack is called
            Then return 400 with the error message
            """)
    void testCreatePack_Exception() {
        Packages mockPack = new Packages();
        mockPack.setName("Fail Package");

        when(packageService.createPack(any(Packages.class)))
                .thenThrow(new RuntimeException("Some error"));

        ResponseEntity<?> response = packagesController.createPack(mockPack);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Some error", response.getBody());
        verify(packageService).createPack(mockPack);
    }

    @Test
    @DisplayName("""
            Given a valid updated pack
            When updateServicePackage is called
            Then the updated pack is returned
            """)
    void testUpdateServicePackage_Success() {
        Packages input = new Packages();
        input.setName("Platinum Package");

        Packages updated = new Packages();
        updated.setId(1L);
        updated.setName("Platinum Package");

        when(packageService.updateServicePackage(eq(1L), any(Packages.class))).thenReturn(updated);

        Packages result = packagesController.updateServicePackage(1L, input);
        assertNotNull(result);
        assertEquals("Platinum Package", result.getName());
        assertEquals(1L, result.getId());
        verify(packageService).updateServicePackage(eq(1L), eq(input));
    }

    @Test
    @DisplayName("""
            Given a pack ID to delete
            When deleteServicePackage is called
            Then it completes with no exceptions
            """)
    void testDeleteServicePackage() {
        doNothing().when(packageService).deleteServicePackage(123L);
        packagesController.deleteServicePackage(123L);
        verify(packageService).deleteServicePackage(123L);
    }
}
