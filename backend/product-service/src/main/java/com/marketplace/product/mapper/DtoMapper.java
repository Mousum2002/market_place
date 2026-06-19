package com.marketplace.product.mapper;

import org.mapstruct.Mapper;

import com.marketplace.product.dto.ProductRequest;
import com.marketplace.product.dto.ProductResponse;
import com.marketplace.product.model.Product;

@Mapper(componentModel = "spring")
public interface DtoMapper {

  ProductRequest productToDto(Product product);

  Product dtoToProduct(ProductRequest productRequest);

  ProductResponse productToResponse(Product product);
}
