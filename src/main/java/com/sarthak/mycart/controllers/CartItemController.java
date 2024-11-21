package com.sarthak.mycart.controllers;

import com.sarthak.mycart.exceptions.ResourceNotFoundException;
import com.sarthak.mycart.response.ApiResponse;
import com.sarthak.mycart.services.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("${api.prefix}/cart-items")
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addCartItem(@RequestParam Long cartId,
                                                   @RequestParam Long productId,
                                                   @RequestParam Integer quantity
    ) {
        try {
            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Cart item added", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Failed to add cart item", e.getMessage()));
        }
    }


    @DeleteMapping("item/remove")
    public ResponseEntity<ApiResponse> removeCartItem(@RequestParam Long cartId,
                                                      @RequestParam Long productId) {
        try {
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Item removed from cart", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Failed to remove item", e.getMessage()));
        }
    }


    @PutMapping("/item/update")
    public ResponseEntity<ApiResponse> updateCartItem(@RequestParam Long cartId,
                                                      @RequestParam Long productId,
                                                      @RequestParam Integer quantity
    ) {
        try {
            cartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Cart item updated", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Failed to update cart item", e.getMessage()));
        }
    }

}
