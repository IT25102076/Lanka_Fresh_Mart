package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.model.Delivery;
import com.lankafreshmart.lanka_fresh_mart.model.DeliveryRoute;
import com.lankafreshmart.lanka_fresh_mart.repository.DeliveryRepository;
import com.lankafreshmart.lanka_fresh_mart.repository.DeliveryRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutingService {

    private final DeliveryRouteRepository routeRepository;
    private final DeliveryRepository deliveryRepository;
    private final com.lankafreshmart.lanka_fresh_mart.repository.DriverRepository driverRepository;

    private static final int MAX_DELIVERIES_PER_ROUTE = 5;

    public List<Delivery> getPendingDeliveriesForDate(LocalDate date) {
        List<Delivery> allDeliveries = deliveryRepository.findAll();
        List<Delivery> pendingDeliveries = new ArrayList<>();
        
        for (Delivery d : allDeliveries) {
            if (d.getScheduledDate().toLocalDate().equals(date) && d.getDeliveryRoute() == null) {
                pendingDeliveries.add(d);
            }
        }
        return pendingDeliveries;
    }

    @Transactional
    public void optimizeRoutesForDate(LocalDate date) {
        // Find all deliveries for this date that don't have a route yet
        List<Delivery> pendingDeliveries = getPendingDeliveriesForDate(date);

        if (pendingDeliveries.isEmpty()) {
            return; // Nothing to route
        }

        // Group into routes of max 5
        int routeCount = 1;
        DeliveryRoute currentRoute = createNewRoute(date, "Route 1");
        
        for (Delivery delivery : pendingDeliveries) {
            if (currentRoute.getDeliveries().size() >= MAX_DELIVERIES_PER_ROUTE) {
                routeRepository.save(currentRoute);
                routeCount++;
                currentRoute = createNewRoute(date, "Route " + routeCount);
            }
            
            delivery.setDeliveryRoute(currentRoute);
            currentRoute.getDeliveries().add(delivery);
            delivery.setStatus(Delivery.Status.DISPATCHED); // Update delivery status to dispatched since it's on a route
            deliveryRepository.save(delivery);
        }
        
        // Save the last route
        if (!currentRoute.getDeliveries().isEmpty()) {
            routeRepository.save(currentRoute);
        }
    }
    
    private DeliveryRoute createNewRoute(LocalDate date, String name) {
        DeliveryRoute route = new DeliveryRoute();
        route.setScheduledDate(date);
        route.setRouteName(name);
        route.setStatus(DeliveryRoute.Status.PENDING);
        return route;
    }

    public List<DeliveryRoute> getRoutesForDate(LocalDate date) {
        return routeRepository.findByScheduledDateOrderByCreatedAtDesc(date);
    }
    
    @Transactional
    public void assignDriver(Long routeId, Long driverId) {
        DeliveryRoute route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        
        // Find driver
        com.lankafreshmart.lanka_fresh_mart.model.Driver driver = null;
        if (driverId != null) {
            driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new RuntimeException("Driver not found"));
        }

        route.setDriver(driver);
        route.setStatus(DeliveryRoute.Status.IN_PROGRESS);
        routeRepository.save(route);
    }

    @Transactional
    public void completeRoute(Long routeId) {
        DeliveryRoute route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        
        if (route.getStatus() != DeliveryRoute.Status.IN_PROGRESS) {
            throw new RuntimeException("Only IN_PROGRESS routes can be completed.");
        }

        route.setStatus(DeliveryRoute.Status.COMPLETED);
        routeRepository.save(route);

        for (Delivery delivery : route.getDeliveries()) {
            delivery.setStatus(Delivery.Status.DELIVERED);
            deliveryRepository.save(delivery);
        }
    }

    @Transactional
    public void deleteRoute(Long routeId) {
        DeliveryRoute route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        // Unlink deliveries and set them back to PREPARING
        for (Delivery delivery : route.getDeliveries()) {
            delivery.setDeliveryRoute(null);
            delivery.setStatus(Delivery.Status.PREPARING);
            deliveryRepository.save(delivery);
        }

        routeRepository.delete(route);
    }
}
