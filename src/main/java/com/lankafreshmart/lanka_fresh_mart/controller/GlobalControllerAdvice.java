package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.model.Cart;
import com.lankafreshmart.lanka_fresh_mart.model.CartItem;
import com.lankafreshmart.lanka_fresh_mart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CartService cartService;

    @ModelAttribute("cartItemCount")
    public int getCartItemCount(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && 
            authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("CUSTOMER"))) {
            try {
                Cart cart = cartService.getCartForUser(authentication.getName());
                if (cart != null && cart.getItems() != null) {
                    return cart.getItems().stream()
                            .mapToInt(CartItem::getQuantity)
                            .sum();
                }
            } catch (Exception e) {
                // Ignore, e.g. user not found in DB
            }
        }
        return 0;
    }
}
