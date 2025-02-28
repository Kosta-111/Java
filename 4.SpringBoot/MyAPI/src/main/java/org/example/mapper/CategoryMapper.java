package org.example.mapper;

import org.example.dto.category.CategoryItemDto;
import org.example.entities.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "creationTime", target = "dateCreated", dateFormat = "yyyy-MM-dd HH:mm:ss")
    CategoryItemDto toDto(CategoryEntity category);

    List<CategoryItemDto> toDto(List<CategoryEntity> categories);
}
