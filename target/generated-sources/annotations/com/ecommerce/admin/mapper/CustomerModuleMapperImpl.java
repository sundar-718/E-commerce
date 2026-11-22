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
import com.ecommerce.admin.entity.Product;
import com.ecommerce.admin.entity.Review;
import com.ecommerce.admin.entity.WishlistItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-21T19:17:44+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.200.v20261008-1115, environment: Java 25.0.4.1 (Eclipse Adoptium)"
)
@Component
public class CustomerModuleMapperImpl implements CustomerModuleMapper {

    @Override
    public CustomerProfileResponse toProfileResponse(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerProfileResponse.CustomerProfileResponseBuilder customerProfileResponse = CustomerProfileResponse.builder();

        customerProfileResponse.id( customer.getId() );
        customerProfileResponse.firstName( customer.getFirstName() );
        customerProfileResponse.lastName( customer.getLastName() );
        customerProfileResponse.email( customer.getEmail() );
        customerProfileResponse.phone( customer.getPhone() );
        customerProfileResponse.profileImage( customer.getProfileImage() );
        customerProfileResponse.status( customer.getStatus() );
        customerProfileResponse.createdAt( customer.getCreatedAt() );
        customerProfileResponse.updatedAt( customer.getUpdatedAt() );

        return customerProfileResponse.build();
    }

    @Override
    public CustomerAddressResponse toAddressResponse(CustomerAddress address) {
        if ( address == null ) {
            return null;
        }

        CustomerAddressResponse.CustomerAddressResponseBuilder customerAddressResponse = CustomerAddressResponse.builder();

        customerAddressResponse.id( address.getId() );
        customerAddressResponse.fullName( address.getFullName() );
        customerAddressResponse.mobile( address.getMobile() );
        customerAddressResponse.addressLine1( address.getAddressLine1() );
        customerAddressResponse.addressLine2( address.getAddressLine2() );
        customerAddressResponse.city( address.getCity() );
        customerAddressResponse.state( address.getState() );
        customerAddressResponse.postalCode( address.getPostalCode() );
        customerAddressResponse.country( address.getCountry() );
        customerAddressResponse.landmark( address.getLandmark() );
        customerAddressResponse.defaultAddress( address.isDefaultAddress() );

        return customerAddressResponse.build();
    }

    @Override
    public CartItemResponse toCartItemResponse(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        CartItemResponse.CartItemResponseBuilder cartItemResponse = CartItemResponse.builder();

        cartItemResponse.id( cartItem.getId() );
        cartItemResponse.productId( cartItemProductId( cartItem ) );
        cartItemResponse.productName( cartItemProductName( cartItem ) );
        cartItemResponse.quantity( cartItem.getQuantity() );
        cartItemResponse.subtotal( cartItem.getSubtotal() );

        return cartItemResponse.build();
    }

    @Override
    public WishlistItemResponse toWishlistItemResponse(WishlistItem wishlistItem) {
        if ( wishlistItem == null ) {
            return null;
        }

        WishlistItemResponse.WishlistItemResponseBuilder wishlistItemResponse = WishlistItemResponse.builder();

        wishlistItemResponse.id( wishlistItem.getId() );
        wishlistItemResponse.productId( wishlistItemProductId( wishlistItem ) );
        wishlistItemResponse.productName( wishlistItemProductName( wishlistItem ) );
        wishlistItemResponse.imageUrl( wishlistItemProductImageUrl( wishlistItem ) );
        wishlistItemResponse.price( wishlistItemProductPrice( wishlistItem ) );

        return wishlistItemResponse.build();
    }

    @Override
    public ReviewResponse toReviewResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewResponse.ReviewResponseBuilder reviewResponse = ReviewResponse.builder();

        reviewResponse.id( review.getId() );
        reviewResponse.productId( reviewProductId( review ) );
        reviewResponse.customerName( reviewCustomerFirstName( review ) );
        reviewResponse.rating( review.getRating() );
        reviewResponse.comment( review.getComment() );
        reviewResponse.createdAt( review.getCreatedAt() );

        return reviewResponse.build();
    }

    @Override
    public OrderSummaryResponse toOrderSummaryResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderSummaryResponse.OrderSummaryResponseBuilder orderSummaryResponse = OrderSummaryResponse.builder();

        orderSummaryResponse.id( order.getId() );
        orderSummaryResponse.orderNumber( order.getOrderNumber() );
        orderSummaryResponse.totalAmount( order.getTotalAmount() );
        if ( order.getPaymentStatus() != null ) {
            orderSummaryResponse.paymentStatus( order.getPaymentStatus().name() );
        }
        if ( order.getOrderStatus() != null ) {
            orderSummaryResponse.orderStatus( order.getOrderStatus().name() );
        }
        orderSummaryResponse.createdAt( order.getCreatedAt() );

        return orderSummaryResponse.build();
    }

    @Override
    public OrderDetailResponse toOrderDetailResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDetailResponse.OrderDetailResponseBuilder orderDetailResponse = OrderDetailResponse.builder();

        orderDetailResponse.id( order.getId() );
        orderDetailResponse.orderNumber( order.getOrderNumber() );
        orderDetailResponse.address( toAddressResponse( order.getAddress() ) );
        orderDetailResponse.subtotal( order.getSubtotal() );
        orderDetailResponse.discount( order.getDiscount() );
        orderDetailResponse.shipping( order.getShipping() );
        orderDetailResponse.tax( order.getTax() );
        orderDetailResponse.totalAmount( order.getTotalAmount() );
        orderDetailResponse.paymentMethod( order.getPaymentMethod() );
        if ( order.getPaymentStatus() != null ) {
            orderDetailResponse.paymentStatus( order.getPaymentStatus().name() );
        }
        if ( order.getOrderStatus() != null ) {
            orderDetailResponse.orderStatus( order.getOrderStatus().name() );
        }
        orderDetailResponse.createdAt( order.getCreatedAt() );
        orderDetailResponse.items( orderItemListToOrderItemResponseList( order.getItems() ) );

        return orderDetailResponse.build();
    }

    @Override
    public OrderItemResponse toOrderItemResponse(OrderItem item) {
        if ( item == null ) {
            return null;
        }

        OrderItemResponse.OrderItemResponseBuilder orderItemResponse = OrderItemResponse.builder();

        orderItemResponse.productId( itemProductId( item ) );
        orderItemResponse.productName( itemProductName( item ) );
        orderItemResponse.quantity( item.getQuantity() );
        orderItemResponse.unitPrice( item.getUnitPrice() );
        orderItemResponse.totalPrice( item.getTotalPrice() );

        return orderItemResponse.build();
    }

    private Long cartItemProductId(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }
        Product product = cartItem.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String cartItemProductName(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }
        Product product = cartItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long wishlistItemProductId(WishlistItem wishlistItem) {
        if ( wishlistItem == null ) {
            return null;
        }
        Product product = wishlistItem.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String wishlistItemProductName(WishlistItem wishlistItem) {
        if ( wishlistItem == null ) {
            return null;
        }
        Product product = wishlistItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String wishlistItemProductImageUrl(WishlistItem wishlistItem) {
        if ( wishlistItem == null ) {
            return null;
        }
        Product product = wishlistItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String imageUrl = product.getImageUrl();
        if ( imageUrl == null ) {
            return null;
        }
        return imageUrl;
    }

    private BigDecimal wishlistItemProductPrice(WishlistItem wishlistItem) {
        if ( wishlistItem == null ) {
            return null;
        }
        Product product = wishlistItem.getProduct();
        if ( product == null ) {
            return null;
        }
        BigDecimal price = product.getPrice();
        if ( price == null ) {
            return null;
        }
        return price;
    }

    private Long reviewProductId(Review review) {
        if ( review == null ) {
            return null;
        }
        Product product = review.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String reviewCustomerFirstName(Review review) {
        if ( review == null ) {
            return null;
        }
        Customer customer = review.getCustomer();
        if ( customer == null ) {
            return null;
        }
        String firstName = customer.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    protected List<OrderItemResponse> orderItemListToOrderItemResponseList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemResponse> list1 = new ArrayList<OrderItemResponse>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( toOrderItemResponse( orderItem ) );
        }

        return list1;
    }

    private Long itemProductId(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Product product = orderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String itemProductName(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Product product = orderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
