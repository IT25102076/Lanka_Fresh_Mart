package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String viewCart(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        model.addAttribute("cart", cartService.getCartForUser(authentication.getName()));
        return "cart/view";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, @RequestParam(defaultValue = "1") int quantity, 
                            Authentication authentication, RedirectAttributes redirectAttributes,
                            @RequestHeader(value = "Referer", required = false) String referer) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        try {
            cartService.addToCart(authentication.getName(), productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Item added to cart!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:" + (referer != null ? referer : "/products");
    }

    @PostMapping("/update/{itemId}")
    public String updateQuantity(@PathVariable Long itemId, @RequestParam int quantity, 
                                 Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            cartService.updateCartItemQuantity(authentication.getName(), itemId, quantity);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove/{itemId}")
    public String removeItem(@PathVariable Long itemId, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            cartService.removeCartItem(authentication.getName(), itemId);
            redirectAttributes.addFlashAttribute("success", "Item removed from cart!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            cartService.clearCart(authentication.getName());
            redirectAttributes.addFlashAttribute("success", "Cart cleared!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }
}
