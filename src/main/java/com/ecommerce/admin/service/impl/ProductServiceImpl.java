package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.PageResponse;
import com.ecommerce.admin.dto.product.ProductCreateRequest;
import com.ecommerce.admin.dto.product.ProductResponse;
import com.ecommerce.admin.dto.product.ProductUpdateRequest;
import com.ecommerce.admin.entity.Category;
import com.ecommerce.admin.entity.Product;
import com.ecommerce.admin.exception.ResourceNotFoundException;
import com.ecommerce.admin.mapper.ProductMapper;
import com.ecommerce.admin.repository.CategoryRepository;
import com.ecommerce.admin.repository.ProductRepository;
import com.ecommerce.admin.service.ProductService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @org.springframework.beans.factory.annotation.Autowired
    @org.springframework.context.annotation.Lazy
    private ProductCachingService productCachingService;

    @Override
    @Cacheable(cacheNames = "products", key = "'products:' + #page + ':' + #size + ':' + #sortBy + ':' + #direction + ':' + #keyword + ':' + #categoryId + ':' + #brand")
    public PageResponse<ProductResponse> getProducts(int page, int size, String sortBy, String direction, String keyword, Long categoryId, String brand) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Product> specification = buildSpecification(keyword, categoryId, brand);
        Page<Product> productPage = productRepository.findAll(specification, pageable);
        return toPageResponse(productPage);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductResponse createProduct(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + request.getCategoryId()));
        Product product = ProductMapper.toEntity(request, category);
        Product saved = productRepository.save(product);
        productCachingService.evictProductCaches();
        log.info("Product created: {}", saved.getName());
        return ProductMapper.toResponse(saved);
    }

    @Override
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return ProductMapper.toResponse(product);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + request.getCategoryId()));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setBrand(request.getBrand());
        product.setActive(request.getActive());
        product.setCategory(category);
        Product saved = productRepository.save(product);
        productCachingService.evictProductCaches();
        log.info("Product updated: {}", saved.getName());
        return ProductMapper.toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        productRepository.delete(product);
        productCachingService.evictProductCaches();
        log.info("Product deleted: {}", product.getName());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductResponse disableProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        product.setActive(false);
        Product saved = productRepository.save(product);
        productCachingService.evictProductCaches();
        log.info("Product disabled: {}", saved.getName());
        return ProductMapper.toResponse(saved);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "products", allEntries = true)
    public ProductResponse enableProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        product.setActive(true);
        Product saved = productRepository.save(product);
        productCachingService.evictProductCaches();
        log.info("Product enabled: {}", saved.getName());
        return ProductMapper.toResponse(saved);
    }

    private Specification<Product> buildSpecification(String keyword, Long categoryId, String brand) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(keyword)) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
                ));
            }
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }
            if (StringUtils.hasText(brand)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), "%" + brand.toLowerCase() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private PageResponse<ProductResponse> toPageResponse(Page<Product> page) {
        return PageResponse.<ProductResponse>builder()
                .content(page.getContent().stream().map(ProductMapper::toResponse).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
