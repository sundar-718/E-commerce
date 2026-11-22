package com.ecommerce.admin.mapper;

import com.ecommerce.admin.dto.customer.CartItemResponse;
import com.ecommerce.admin.dto.customer.CustomerAddressResponse;
import com.ecommerce.admin.dto.customer.CustomerProfileResponse;
import com.ecommerce.admin.dto.customer.OrderDetailResponse;
import com.ecommerce.admin.dto.customer.OrderItemResponse;
import com.ecommerce.admin.dto.customer.OrderSummaryResponse;
import com.ecommerce.admin.dto.customer.ReviewResponse;
import com.ecommerce.admin.dto.customer.WishlistItemResponse;
import com.ecommerce.admin.entity.CartItem;
import com.ecommerce.admin.entity.Customer;
import com.ecommerce.admin.entity.CustomerAddress;
import com.ecommerce.admin.entity.Order;
import com.ecommerce.admin.entity.OrderItem;
import com.ecommerce.admin.entity.Review;
import com.ecommerce.admin.entity.WishlistItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerModuleMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "profileImage", source = "profileImage")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    CustomerProfileResponse toProfileResponse(Customer customer);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "mobile", source = "mobile")
    @Mapping(target = "addressLine1", source = "addressLine1")
    @Mapping(target = "addressLine2", source = "addressLine2")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "postalCode", source = "postalCode")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "landmark", source = "landmark")
    @Mapping(target = "defaultAddress", source = "defaultAddress")
    CustomerAddressResponse toAddressResponse(CustomerAddress address);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "subtotal", source = "subtotal")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "imageUrl", source = "product.imageUrl")
    @Mapping(target = "price", source = "product.price")
    WishlistItemResponse toWishlistItemResponse(WishlistItem wishlistItem);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "customerName", source = "customer.firstName")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "comment", source = "comment")
    @Mapping(target = "createdAt", source = "createdAt")
    ReviewResponse toReviewResponse(Review review);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "orderNumber", source = "orderNumber")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "paymentStatus", source = "paymentStatus")
    @Mapping(target = "orderStatus", source = "orderStatus")
    @Mapping(target = "createdAt", source = "createdAt")
    OrderSummaryResponse toOrderSummaryResponse(Order order);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "orderNumber", source = "orderNumber")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "subtotal", source = "subtotal")
    @Mapping(target = "discount", source = "discount")
    @Mapping(target = "shipping", source = "shipping")
    @Mapping(target = "tax", source = "tax")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "paymentStatus", source = "paymentStatus")
    @Mapping(target = "orderStatus", source = "orderStatus")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "items", source = "items")
    OrderDetailResponse toOrderDetailResponse(Order order);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "totalPrice", source = "totalPrice")
    OrderItemResponse toOrderItemResponse(OrderItem item);
}
