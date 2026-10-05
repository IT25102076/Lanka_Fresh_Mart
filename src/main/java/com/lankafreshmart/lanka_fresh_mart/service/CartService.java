package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.model.Cart;
import com.lankafreshmart.lanka_fresh_mart.model.CartItem;
import com.lankafreshmart.lanka_fresh_mart.model.Product;
import com.lankafreshmart.lanka_fresh_mart.model.User;
import com.lankafreshmart.lanka_fresh_mart.repository.CartRepository;
import com.lankafreshmart.lanka_fresh_mart.repository.CartItemRepository;
import com.lankafreshmart.lanka_fresh_mart.repository.ProductRepository;
import com.lankafreshmart.lanka_fresh_mart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Cart getCartForUser(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    @Transactional
    public void addToCart(String username, Long productId, int quantity) {
        Cart cart = getCartForUser(username);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        int totalRequested = quantity;
        if (existingItem.isPresent()) {
            totalRequested += existingItem.get().getQuantity();
        }

        if (product.getAvailability() == Product.Availability.UNAVAILABLE || product.getQuantityOnHand() < totalRequested) {
            throw new RuntimeException("Insufficient stock. Only " + product.getQuantityOnHand() + " " + product.getUnit() + " available");
        }



        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setPriceAtTime(product.getPrice()); // Update to current price
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPriceAtTime(product.getPrice());
            cart.getItems().add(newItem);
        }
        
        cartRepository.save(cart);
    }

    @Transactional
    public void updateCartItemQuantity(String username, Long cartItemId, int quantity) {
        Cart cart = getCartForUser(username);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Unauthorized to modify this cart item");
        }

        if (quantity <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            if (item.getProduct().getQuantityOnHand() < quantity) {
                throw new RuntimeException("Insufficient stock. Only " + item.getProduct().getQuantityOnHand() + " " + item.getProduct().getUnit() + " available");
            }
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        cartRepository.save(cart);
    }

    @Transactional
    public void removeCartItem(String username, Long cartItemId) {
        Cart cart = getCartForUser(username);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Unauthorized to modify this cart item");
        }

        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(String username) {
        Cart cart = getCartForUser(username);
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
