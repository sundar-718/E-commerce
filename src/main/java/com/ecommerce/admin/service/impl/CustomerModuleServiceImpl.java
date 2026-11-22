package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.PageResponse;
import com.ecommerce.admin.dto.customer.CartItemRequest;
import com.ecommerce.admin.dto.customer.CartItemResponse;
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
import com.ecommerce.admin.entity.CartItem;
import com.ecommerce.admin.entity.Customer;
import com.ecommerce.admin.entity.CustomerAddress;
import com.ecommerce.admin.entity.CustomerStatus;
import com.ecommerce.admin.entity.Order;
import com.ecommerce.admin.entity.OrderItem;
import com.ecommerce.admin.entity.OrderStatus;
import com.ecommerce.admin.entity.PaymentStatus;
import com.ecommerce.admin.entity.Product;
import com.ecommerce.admin.entity.Review;
import com.ecommerce.admin.entity.User;
import com.ecommerce.admin.entity.WishlistItem;
import com.ecommerce.admin.exception.DuplicateResourceException;
import com.ecommerce.admin.exception.InvalidOrderStateException;
import com.ecommerce.admin.exception.ResourceNotFoundException;
import com.ecommerce.admin.exception.UnauthorizedException;
import com.ecommerce.admin.mapper.CustomerModuleMapper;
import com.ecommerce.admin.repository.CartItemRepository;
import com.ecommerce.admin.repository.CustomerAddressRepository;
import com.ecommerce.admin.repository.CustomerRepository;
import com.ecommerce.admin.repository.OrderItemRepository;
import com.ecommerce.admin.repository.UserRepository;
import com.ecommerce.admin.repository.OrderRepository;
import com.ecommerce.admin.repository.ProductRepository;
import com.ecommerce.admin.repository.ReviewRepository;
import com.ecommerce.admin.repository.WishlistItemRepository;
import com.ecommerce.admin.service.CustomerModuleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerModuleServiceImpl implements CustomerModuleService {

    private static final Logger log = LoggerFactory.getLogger(CustomerModuleServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CartItemRepository cartItemRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;
    private final CustomerModuleMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CustomerProfileResponse register(CustomerRegistrationRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Customer already exists");
        }
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(CustomerStatus.ACTIVE)
                .role("ROLE_CUSTOMER")
                .build();
        Customer saved = customerRepository.save(customer);
        userRepository.save(User.builder()
                .email(saved.getEmail())
                .password(saved.getPassword())
                .role(saved.getRole())
                .build());
        log.info("Customer registered: {}", saved.getEmail());
        return mapper.toProfileResponse(saved);
    }

    @Override
    public CustomerProfileResponse getCurrentCustomerProfile() {
        Customer customer = currentCustomer();
        return mapper.toProfileResponse(customer);
    }

    @Override
    @Transactional
    public CustomerProfileResponse updateCurrentCustomerProfile(CustomerProfileUpdateRequest request) {
        Customer customer = currentCustomer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        Customer saved = customerRepository.save(customer);
        log.info("Customer profile updated: {}", saved.getEmail());
        return mapper.toProfileResponse(saved);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        Customer customer = currentCustomer();
        if (!passwordEncoder.matches(request.getCurrentPassword(), customer.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }
        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerRepository.save(customer);
        userRepository.findByEmail(customer.getEmail()).ifPresent(user -> {
            user.setPassword(customer.getPassword());
            userRepository.save(user);
        });
        log.info("Customer password changed: {}", customer.getEmail());
    }

    @Override
    @Transactional
    public void deleteCurrentAccount() {
        Customer customer = currentCustomer();
        customerRepository.delete(customer);
    }

    @Override
    public List<CustomerAddressResponse> getAddresses() {
        return customerAddressRepository.findByCustomerId(currentCustomer().getId()).stream()
                .map(mapper::toAddressResponse)
                .toList();
    }

    @Override
    @Transactional
    public CustomerAddressResponse addAddress(CustomerAddressRequest request) {
        Customer customer = currentCustomer();
        CustomerAddress address = CustomerAddress.builder()
                .fullName(request.getFullName())
                .mobile(request.getMobile())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .landmark(request.getLandmark())
                .defaultAddress(request.isDefaultAddress())
                .customer(customer)
                .build();
        if (request.isDefaultAddress()) {
            customerAddressRepository.clearDefaultAddresses(customer.getId());
        }
        return mapper.toAddressResponse(customerAddressRepository.save(address));
    }

    @Override
    @Transactional
    public CustomerAddressResponse updateAddress(Long addressId, CustomerAddressRequest request) {
        CustomerAddress address = addressForCustomer(addressId);
        address.setFullName(request.getFullName());
        address.setMobile(request.getMobile());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setLandmark(request.getLandmark());
        if (request.isDefaultAddress()) {
            customerAddressRepository.clearDefaultAddresses(address.getCustomer().getId());
        }
        address.setDefaultAddress(request.isDefaultAddress());
        return mapper.toAddressResponse(customerAddressRepository.save(address));
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        CustomerAddress address = addressForCustomer(addressId);
        customerAddressRepository.delete(address);
    }

    @Override
    @Transactional
    public CustomerAddressResponse setDefaultAddress(Long addressId) {
        CustomerAddress address = addressForCustomer(addressId);
        customerAddressRepository.clearDefaultAddresses(address.getCustomer().getId());
        address.setDefaultAddress(true);
        return mapper.toAddressResponse(customerAddressRepository.save(address));
    }

    @Override
    public CartResponse getCart() {
        List<CartItem> items = cartItemRepository.findByCustomerId(currentCustomer().getId());
        List<CartItemResponse> dtoItems = items.stream().map(mapper::toCartItemResponse).toList();
        BigDecimal total = items.stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return CartResponse.builder()
                .totalItems(items.size())
                .totalPrice(total)
                .cartItems(dtoItems)
                .build();
    }

    @Override
    @Transactional
    public CartResponse addToCart(CartItemRequest request) {
        Customer customer = currentCustomer();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (product.getStock() == null || product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        Optional<CartItem> existing = cartItemRepository.findByCustomerIdAndProductId(customer.getId(), product.getId());
        CartItem cartItem;
        if (existing.isPresent()) {
            cartItem = existing.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock");
            }
        } else {
            cartItem = CartItem.builder()
                    .customer(customer)
                    .product(product)
                    .quantity(request.getQuantity())
                    .subtotal(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                    .build();
        }
        cartItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        cartItemRepository.save(cartItem);
        log.info("Cart updated: {}", customer.getEmail());
        return getCart();
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(Long cartItemId, CartItemRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (!cartItem.getCustomer().getId().equals(currentCustomer().getId())) {
            throw new UnauthorizedException("You cannot update this cart item");
        }
        Product product = cartItem.getProduct();
        if (product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        cartItem.setQuantity(request.getQuantity());
        cartItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        cartItemRepository.save(cartItem);
        return getCart();
    }

    @Override
    @Transactional
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (!cartItem.getCustomer().getId().equals(currentCustomer().getId())) {
            throw new UnauthorizedException("You cannot delete this cart item");
        }
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void clearCart() {
        cartItemRepository.deleteAllByCustomerId(currentCustomer().getId());
    }

    @Override
    public List<WishlistItemResponse> getWishlist() {
        return wishlistItemRepository.findByCustomerId(currentCustomer().getId()).stream()
                .map(mapper::toWishlistItemResponse)
                .toList();
    }

    @Override
    @Transactional
    public WishlistItemResponse addToWishlist(Long productId) {
        Customer customer = currentCustomer();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (wishlistItemRepository.existsByCustomerIdAndProductId(customer.getId(), productId)) {
            throw new DuplicateResourceException("Product already in wishlist");
        }
        WishlistItem item = WishlistItem.builder()
                .customer(customer)
                .product(product)
                .build();
        return mapper.toWishlistItemResponse(wishlistItemRepository.save(item));
    }

    @Override
    @Transactional
    public void removeFromWishlist(Long productId) {
        WishlistItem item = wishlistItemRepository.findByCustomerIdAndProductId(currentCustomer().getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist item not found"));
        wishlistItemRepository.delete(item);
    }

    @Override
    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        Customer customer = currentCustomer();
        List<CartItem> cartItems = cartItemRepository.findByCustomerId(customer.getId());
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }
        CustomerAddress address = customerAddressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new UnauthorizedException("Address does not belong to current customer");
        }
        BigDecimal subtotal = cartItems.stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal shipping = subtotal.compareTo(BigDecimal.valueOf(100)) >= 0 ? BigDecimal.ZERO : new BigDecimal("10.00");
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.08"));
        BigDecimal totalAmount = subtotal.add(shipping).add(tax).subtract(discount);

        Order order = Order.builder()
                .orderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .customer(customer)
                .address(address)
                .subtotal(subtotal)
                .discount(discount)
                .shipping(shipping)
                .tax(tax)
                .totalAmount(totalAmount)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .orderStatus(OrderStatus.PENDING)
                .build();
        Order savedOrder = orderRepository.save(order);
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalStateException("Insufficient stock");
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(product.getPrice())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                    .build();
            orderItemRepository.save(orderItem);
        }
        cartItemRepository.deleteAllByCustomerId(customer.getId());
        log.info("Order placed: {}", savedOrder.getOrderNumber());
        return CheckoutResponse.builder()
                .orderId(savedOrder.getId())
                .orderNumber(savedOrder.getOrderNumber())
                .subtotal(savedOrder.getSubtotal())
                .discount(savedOrder.getDiscount())
                .shipping(savedOrder.getShipping())
                .tax(savedOrder.getTax())
                .totalAmount(savedOrder.getTotalAmount())
                .paymentMethod(savedOrder.getPaymentMethod())
                .orderStatus(savedOrder.getOrderStatus().name())
                .build();
    }

    @Override
    public PageResponse<OrderSummaryResponse> getOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> pageResult = orderRepository.findByCustomerId(currentCustomer().getId(), pageable);
        return PageResponse.<OrderSummaryResponse>builder()
                .content(pageResult.getContent().stream().map(mapper::toOrderSummaryResponse).toList())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .first(pageResult.isFirst())
                .last(pageResult.isLast())
                .build();
    }

    @Override
    public OrderDetailResponse getOrder(Long orderId) {
        Order order = orderRepository.findByIdAndCustomerId(orderId, currentCustomer().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapper.toOrderDetailResponse(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findByIdAndCustomerId(orderId, currentCustomer().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getOrderStatus() != OrderStatus.PENDING && order.getOrderStatus() != OrderStatus.CONFIRMED) {
            throw new InvalidOrderStateException("Order cannot be cancelled at this stage");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setPaymentStatus(PaymentStatus.REFUNDED);
        orderRepository.save(order);
        log.info("Order cancelled: {}", order.getOrderNumber());
    }

    @Override
    @Transactional
    public ReviewResponse createReview(Long productId, ReviewRequest request) {
        Customer customer = currentCustomer();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (reviewRepository.existsByCustomerIdAndProductId(customer.getId(), productId)) {
            throw new DuplicateResourceException("You have already reviewed this product");
        }
        boolean purchased = orderItemRepository.existsByProductIdAndOrderCustomerId(productId, customer.getId());
        if (!purchased) {
            throw new UnauthorizedException("You can only review purchased products");
        }
        Review review = Review.builder()
                .customer(customer)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        Review saved = reviewRepository.save(review);
        log.info("Review created: {}", saved.getId());
        return mapper.toReviewResponse(saved);
    }

    @Override
    public List<ReviewResponse> getProductReviews(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(mapper::toReviewResponse)
                .toList();
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        if (!review.getCustomer().getId().equals(currentCustomer().getId())) {
            throw new UnauthorizedException("You cannot update this review");
        }
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        return mapper.toReviewResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        if (!review.getCustomer().getId().equals(currentCustomer().getId())) {
            throw new UnauthorizedException("You cannot delete this review");
        }
        reviewRepository.delete(review);
    }

    private Customer currentCustomer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Customer not authenticated"));
    }

    private CustomerAddress addressForCustomer(Long addressId) {
        CustomerAddress address = customerAddressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        if (!address.getCustomer().getId().equals(currentCustomer().getId())) {
            throw new UnauthorizedException("Address does not belong to current customer");
        }
        return address;
    }
}
