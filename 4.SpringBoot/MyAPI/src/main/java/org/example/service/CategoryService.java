package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dto.category.CategoryItemDto;
import org.example.entities.CategoryEntity;
import org.example.dto.category.CategoryPostDto;
import org.example.mapper.CategoryMapper;
import org.example.repository.ICategoryRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private FileService fileService;
    private CategoryMapper categoryMapper;
    private ICategoryRepository categoryRepository;

    public List<CategoryItemDto> getAllCategories() {
        var entities = categoryRepository.findAll();
        return categoryMapper.toDto(entities);
    }

    public CategoryItemDto getCategoryById(Integer id) {
        var res = categoryRepository.findById(id);
        return res.isPresent()
                ? categoryMapper.toDto(res.get())
                : null;
    }

    public CategoryItemDto createCategory(CategoryPostDto category) {
        var entity = new CategoryEntity();
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());
        entity.setCreationTime(LocalDateTime.now());

        var newImageFile = category.getImageFile();
        if (newImageFile != null && !newImageFile.isEmpty()){
            var imagePath = fileService.load(newImageFile);
            entity.setImage(imagePath);
        }
        categoryRepository.save(entity);
        return categoryMapper.toDto(entity);
    }

    public boolean updateCategory(Integer id, CategoryPostDto category) {
        var res = categoryRepository.findById(id);
        if (res.isEmpty()){
            return false;
        }
        var entity = res.get();
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());

        var newImageFile = category.getImageFile();
        if (newImageFile != null && !newImageFile.isEmpty()){
            var newImagePath = fileService.replace(entity.getImage(), newImageFile);
            entity.setImage(newImagePath);
        }
        categoryRepository.save(entity);
        return true;
    }

    public boolean deleteCategory(Integer id) {
        var res = categoryRepository.findById(id);
        if (res.isEmpty()){
            return false;
        }
        fileService.remove(res.get().getImage());
        categoryRepository.deleteById(id);
        return true;
    }
}