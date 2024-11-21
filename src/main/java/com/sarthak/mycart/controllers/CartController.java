package com.sarthak.mycart.controllers;

import com.sarthak.mycart.entities.Cart;
import com.sarthak.mycart.exceptions.ResourceNotFoundException;
import com.sarthak.mycart.response.ApiResponse;
import com.sarthak.mycart.services.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.MessageFormat;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try {
            log.info(MessageFormat.format("_____________> Fetching cart with id {0}", cartId));
            Cart cart = cartService.getCart(cartId);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Cart fetched.", cart));
        } catch (ResourceNotFoundException e) {
            log.info(MessageFormat.format("_____________> Error fetching cart with id {0}", cartId));
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Failed to get cart", e.getMessage()));
        }
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        try {
            cartService.clearCart(cartId);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Cleared cart", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Failed to clear cart", e.getMessage()));
        }
    }

    @GetMapping("/{cartId}/cart/total-amount")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        try {
            BigDecimal amount = cartService.getTotalPrice(cartId);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Amount fetched", amount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Failed to get total amount", e.getMessage()));
        }
    }
}
