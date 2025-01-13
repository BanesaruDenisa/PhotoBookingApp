package com.example.photobooking.services;

import com.example.photobooking.model.entities.Packages;
import com.example.photobooking.repositories.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackagesService {

    @Autowired
    private PackageRepository packageRepository;

    public List<Packages> getAllPacks() {
        return packageRepository.findAll();
    }

    public Packages getPackById(Long id) {
        return packageRepository.findById(id).orElse(null);
    }

    public PackagesService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public Packages findById(Long id) {
        return packageRepository.findById(id).orElse(null);
    }

    public Packages createPack(Packages Packages) {
        // Validăm dacă pack-ul este nou
        if (Packages.getId() != null && packageRepository.existsById(Packages.getId())) {
            throw new IllegalArgumentException("Pack with this ID already exists");
        }
        return packageRepository.save(Packages);
    }

    public Packages updateServicePackage(Long id, Packages updatedPackages) {
        //checkAdminRole(); // Doar admin poate edita pachete
        return packageRepository.findById(id)
                .map(existingPack -> {
                    existingPack.setName(updatedPackages.getName());
                    existingPack.setDescription(updatedPackages.getDescription());
                    existingPack.setPrice(updatedPackages.getPrice());
                    return packageRepository.save(existingPack);
                })
                .orElse(null);
    }

    public void deleteServicePackage(Long id) {
        packageRepository.deleteById(id);
    }

}
