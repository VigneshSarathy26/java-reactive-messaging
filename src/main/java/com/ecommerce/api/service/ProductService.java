package com.ecommerce.api.service;

import com.ecommerce.api.dto.ProductDTO;
import com.ecommerce.api.mapper.ProductMapper;
import com.ecommerce.api.model.UserActivity;
import com.ecommerce.api.repository.ActivityRepository;
import com.ecommerce.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ActivityRepository activityRepository;
    private final ProductMapper productMapper;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public Flux<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .map(productMapper::toDTO);
    }

    public Mono<ProductDTO> getProductById(Long id) {
        // Log activity reactive-ly to Cassandra
        return activityRepository.save(UserActivity.builder()
                .id(UUID.randomUUID())
                .userId("anonymous")
                .activityType("VIEW_PRODUCT")
                .metadata("Product ID: " + id)
                .timestamp(Instant.now())
                .build())
                .then(productRepository.findById(id))
                .map(productMapper::toDTO)
                .doOnNext(product -> {
                    // Update cache in Redis
                    redisTemplate.opsForValue().set("product:" + id, product.getName()).subscribe();
                });
    }

    public Mono<ProductDTO> createProduct(ProductDTO productDTO) {
        return productRepository.save(productMapper.toEntity(productDTO))
                .map(productMapper::toDTO);
    }
}
