package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.model.Cart;
import com.lankafreshmart.lanka_fresh_mart.model.CartItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class StripeService {

    @Value("${stripe.secret.key:}")
    private String secretKey;

    @Value("${app.baseUrl:http://localhost:8080}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public Session createCheckoutSession(Cart cart, String deliveryAddress) throws StripeException {
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity((long) item.getQuantity())
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("lkr")
                                    .setUnitAmount(item.getPriceAtTime().multiply(new BigDecimal(100)).longValue())
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(item.getProduct().getName())
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();
            lineItems.add(lineItem);
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(baseUrl + "/checkout/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(baseUrl + "/checkout/cancel")
                .addAllLineItem(lineItems)
                .putMetadata("deliveryAddress", deliveryAddress)
                .build();

        return Session.create(params);
    }
    
    public Session retrieveSession(String sessionId) throws StripeException {
        return Session.retrieve(sessionId);
    }
}
