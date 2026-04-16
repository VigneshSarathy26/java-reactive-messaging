package com.ecommerce.api.mapper;

import com.ecommerce.api.dto.ProductDTO;
import com.ecommerce.api.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    Product toEntity(ProductDTO productDTO);
}
