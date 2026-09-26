package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.model.Product;
import com.lankafreshmart.lanka_fresh_mart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchApiController {

    private final ProductService productService;

    @GetMapping
    public List<Map<String, Object>> searchSuggestions(@RequestParam String query) {
        if (query == null || query.trim().length() < 2) {
            return List.of();
        }
        List<Product> results = productService.searchProducts(query.trim());
        return results.stream()
                .limit(6)
                .map(p -> Map.<String, Object>of(
                        "id", p.getId(),
                        "name", p.getName(),
                        "category", p.getCategory().name(),
                        "price", p.getPrice(),
                        "unit", p.getUnit(),
                        "imageUrl", p.getImageUrl() != null ? p.getImageUrl() : ""
                ))
                .collect(Collectors.toList());
    }
}
