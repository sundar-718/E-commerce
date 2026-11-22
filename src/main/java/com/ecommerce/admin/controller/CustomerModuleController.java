package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.PageResponse;
import com.ecommerce.admin.dto.customer.CartItemRequest;
import com.ecommerce.admin.dto.customer.CartResponse;
import com.ecommerce.admin.dto.customer.ChangePasswordRequest;
import com.ecommerce.admin.dto.customer.CheckoutRequest;
import com.ecommerce.admin.dto.customer.CheckoutResponse;
import com.ecommerce.admin.dto.customer.CustomerAddressRequest;
import com.ecommerce.admin.dto.customer.CustomerAddressResponse;
import com.ecommerce.admin.dto.customer.CustomerProfileResponse;
import com.ecommerce.admin.dto.customer.CustomerProfileUpdateRequest;
import com.ecommerce.admin.dto.customer.CustomerRegistrationRequest;
import com.ecommerce.admin.dto.customer.OrderDetailResponse;
import com.ecommerce.admin.dto.customer.OrderSummaryResponse;
import com.ecommerce.admin.dto.customer.ReviewRequest;
import com.ecommerce.admin.dto.customer.ReviewResponse;
import com.ecommerce.admin.dto.customer.WishlistItemResponse;
import com.ecommerce.admin.service.CustomerModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerModuleController {

    private final CustomerModuleService customerModuleService;

    @PostMapping("/auth/register")
    public ResponseEntity<CustomerProfileResponse> register(@Valid @RequestBody CustomerRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerModuleService.register(request));
    }

    @GetMapping("/profile")
    public ResponseEntity<CustomerProfileResponse> getProfile() {
        return ResponseEntity.ok(customerModuleService.getCurrentCustomerProfile());
    }

    @PutMapping("/profile")
    public ResponseEntity<CustomerProfileResponse> updateProfile(@Valid @RequestBody CustomerProfileUpdateRequest request) {
        return ResponseEntity.ok(customerModuleService.updateCurrentCustomerProfile(request));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        customerModuleService.changePassword(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount() {
        customerModuleService.deleteCurrentAccount();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/addresses")
    public ResponseEntity<CustomerAddressResponse> addAddress(@Valid @RequestBody CustomerAddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerModuleService.addAddress(request));
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<CustomerAddressResponse>> getAddresses() {
        return ResponseEntity.ok(customerModuleService.getAddresses());
    }

    @GetMapping("/addresses/{id}")
    public ResponseEntity<CustomerAddressResponse> getAddress(@PathVariable Long id) {
        return ResponseEntity.ok(customerModuleService.getAddresses().stream()
                .filter(address -> address.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found")));
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<CustomerAddressResponse> updateAddress(@PathVariable Long id, @Valid @RequestBody CustomerAddressRequest request) {
        return ResponseEntity.ok(customerModuleService.updateAddress(id, request));
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        customerModuleService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/addresses/{id}/default")
    public ResponseEntity<CustomerAddressResponse> setDefaultAddress(@PathVariable Long id) {
        return ResponseEntity.ok(customerModuleService.setDefaultAddress(id));
    }

    @PostMapping("/cart/add")
    public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(customerModuleService.addToCart(request));
    }

    @GetMapping("/cart")
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(customerModuleService.getCart());
    }

    @PutMapping("/cart/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(@PathVariable Long cartItemId, @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(customerModuleService.updateCartItem(cartItemId, request));
    }

    @DeleteMapping("/cart/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) {
        customerModuleService.deleteCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cart/clear")
    public ResponseEntity<Void> clearCart() {
        customerModuleService.clearCart();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/wishlist/{productId}")
    public ResponseEntity<WishlistItemResponse> addToWishlist(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerModuleService.addToWishlist(productId));
    }

    @GetMapping("/wishlist")
    public ResponseEntity<List<WishlistItemResponse>> getWishlist() {
        return ResponseEntity.ok(customerModuleService.getWishlist());
    }

    @DeleteMapping("/wishlist/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long productId) {
        customerModuleService.removeFromWishlist(productId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/orders")
    public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerModuleService.checkout(request));
    }

    @GetMapping("/orders")
    public ResponseEntity<PageResponse<OrderSummaryResponse>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(customerModuleService.getOrders(page, size));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDetailResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(customerModuleService.getOrder(id));
    }

    @PatchMapping("/orders/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        customerModuleService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<ReviewResponse> createReview(@PathVariable Long productId, @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerModuleService.createReview(productId, request));
    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(customerModuleService.getProductReviews(productId));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long reviewId, @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(customerModuleService.updateReview(reviewId, request));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        customerModuleService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
