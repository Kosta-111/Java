package org.example.mapper;

import org.example.dto.ProductDto;
import org.example.entites.ProductEntity;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductDto toDTO(ProductEntity productEntity);

    List<ProductDto> toDTO(List<ProductEntity> productEntities);

    ProductEntity toEntity(ProductDto productDto);
}
