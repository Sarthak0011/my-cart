package com.sarthak.mycart.controllers;

import com.sarthak.mycart.entities.Cart;
import com.sarthak.mycart.exceptions.ResourceNotFoundException;
import com.sarthak.mycart.response.ApiResponse;
import com.sarthak.mycart.services.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        try{
            Cart cart = cartService.getCart(cartId);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Cart fetched.", cart));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Failed to get cart", e.getMessage()));
        }
    }
}
