package org.example.controller;

import org.example.dto.CategoryItemDto;
import org.example.dto.CategoryPostDto;
import org.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryItemDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryItemDto> getCategoryById(@PathVariable Integer id) {
        var categoryDto = categoryService.getCategoryById(id);
        return categoryDto != null
                ? new ResponseEntity<>(categoryDto, HttpStatus.OK)
                : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryItemDto> createCategory(@ModelAttribute CategoryPostDto category) {
        var categoryDto = categoryService.createCategory(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCategory(@PathVariable Integer id, @ModelAttribute CategoryPostDto category) {
        return categoryService.updateCategory(id, category)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        return categoryService.deleteCategory(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
