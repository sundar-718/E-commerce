package com.ecommerce.admin;

import com.ecommerce.admin.dto.category.CategoryCreateRequest;
import com.ecommerce.admin.dto.customer.CustomerRegistrationRequest;
import com.ecommerce.admin.dto.product.ProductCreateRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationTests {

    private final Validator validator;

    ValidationTests() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void categoryNameIsRequired() {
        CategoryCreateRequest request = CategoryCreateRequest.builder().name(" ").build();

        Set<?> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void productPriceAndStockMustBeValid() {
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("Phone")
                .price(BigDecimal.ZERO)
                .stock(-1)
                .active(true)
                .categoryId(1L)
                .build();

        Set<?> violations = validator.validate(request);

        assertTrue(violations.size() >= 2);
    }

    @Test
    void customerRegistrationRequiresValidFields() {
        CustomerRegistrationRequest request = CustomerRegistrationRequest.builder()
                .firstName(" ")
                .lastName("Doe")
                .email("not-an-email")
                .password("weak")
                .phone("123")
                .build();

        Set<?> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }
}
