package com.ecommerce.admin.service;

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
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerModuleService {
    CustomerProfileResponse register(CustomerRegistrationRequest request);

    CustomerProfileResponse getCurrentCustomerProfile();

    CustomerProfileResponse updateCurrentCustomerProfile(CustomerProfileUpdateRequest request);

    void changePassword(ChangePasswordRequest request);

    void deleteCurrentAccount();

    List<CustomerAddressResponse> getAddresses();

    CustomerAddressResponse addAddress(CustomerAddressRequest request);

    CustomerAddressResponse updateAddress(Long addressId, CustomerAddressRequest request);

    void deleteAddress(Long addressId);

    CustomerAddressResponse setDefaultAddress(Long addressId);

    CartResponse getCart();

    CartResponse addToCart(CartItemRequest request);

    CartResponse updateCartItem(Long cartItemId, CartItemRequest request);

    void deleteCartItem(Long cartItemId);

    void clearCart();

    List<WishlistItemResponse> getWishlist();

    WishlistItemResponse addToWishlist(Long productId);

    void removeFromWishlist(Long productId);

    CheckoutResponse checkout(CheckoutRequest request);

    PageResponse<OrderSummaryResponse> getOrders(int page, int size);

    OrderDetailResponse getOrder(Long orderId);

    void cancelOrder(Long orderId);

    ReviewResponse createReview(Long productId, ReviewRequest request);

    List<ReviewResponse> getProductReviews(Long productId);

    ReviewResponse updateReview(Long reviewId, ReviewRequest request);

    void deleteReview(Long reviewId);
}
