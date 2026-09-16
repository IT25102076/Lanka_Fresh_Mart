package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.model.Driver;
import com.lankafreshmart.lanka_fresh_mart.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
    }

    @Transactional
    public void saveDriver(Driver driver) {
        driverRepository.save(driver);
    }

    @Transactional
    public void deleteDriver(Long id) {
        Driver driver = getDriverById(id);
        
        if (!driver.getDeliveryRoutes().isEmpty() || !driver.getDeliveries().isEmpty()) {
            throw new RuntimeException("Cannot delete driver because they have active deliveries or routes assigned.");
        }
        
        driverRepository.delete(driver);
    }
}
