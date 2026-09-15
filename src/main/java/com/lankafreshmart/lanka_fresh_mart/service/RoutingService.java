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

    private final DeliveryRepository deliveryRepository;
    private final DeliveryRouteRepository routeRepository;

    private static final int MAX_DELIVERIES_PER_ROUTE = 5;

    @Transactional
    public void optimizeRoutesForDate(LocalDate date) {
        // Find all deliveries for this date that don't have a route yet
        List<Delivery> allDeliveries = deliveryRepository.findAll();
        List<Delivery> pendingDeliveries = new ArrayList<>();
        
        for (Delivery d : allDeliveries) {
            if (d.getScheduledDate().toLocalDate().equals(date) && d.getDeliveryRoute() == null) {
                pendingDeliveries.add(d);
            }
        }

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
    public void assignDriver(Long routeId, String driverName) {
        DeliveryRoute route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        route.setDriverName(driverName);
        route.setStatus(DeliveryRoute.Status.IN_PROGRESS);
        routeRepository.save(route);
    }
}
