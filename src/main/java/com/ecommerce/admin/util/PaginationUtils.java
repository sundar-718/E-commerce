package com.ecommerce.admin.util;

import com.ecommerce.admin.dto.PageResponse;
import org.springframework.data.domain.Page;

public final class PaginationUtils {

    private PaginationUtils() {
    }

    public static <T> PageResponse<T> toPageResponse(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
