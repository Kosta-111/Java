package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dto.product.ProductItemDto;
import org.example.dto.product.ProductPostDto;
import org.example.entities.CategoryEntity;
import org.example.entities.ProductEntity;
import org.example.entities.ProductImageEntity;
import org.example.mapper.ProductMapper;
import org.example.repository.ICategoryRepository;
import org.example.repository.IProductImageRepository;
import org.example.repository.IProductRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private FileService fileService;
    private ProductMapper productMapper;
    private IProductRepository productRepository;
    private ICategoryRepository categoryRepository;
    private IProductImageRepository productImageRepository;

    public List<ProductItemDto> getAllProducts() {
        var entities = productRepository.findAll();
        return productMapper.toDto(entities);
    }

    public ProductItemDto getProductById(Integer id) {
        var res = productRepository.findById(id);
        return res.isPresent()
                ? productMapper.toDto(res.get())
                : null;
    }

    public ProductItemDto createProduct(ProductPostDto product) {
        var entity = new ProductEntity();
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setCreationTime(LocalDateTime.now());
        entity.setAmount(product.getAmount());
        entity.setPrice(product.getPrice());

        var categoryId = product.getCategoryId();
        if (categoryRepository.existsById(categoryId)){
            var category = new CategoryEntity();
            category.setId(categoryId);
            entity.setCategory(category);
        }
        productRepository.save(entity);

        var imageFiles = product.getImageFiles();
        if (imageFiles != null) {
            var priority = 1;
            for (var file : imageFiles) {
                if (file == null || file.isEmpty()) continue;
                var imageName = fileService.load(file);
                var img = new ProductImageEntity();
                img.setPriority(priority++);
                img.setName(imageName);
                img.setProduct(entity);
                productImageRepository.save(img);
            }
        }
        return productMapper.toDto(entity);
    }

    public boolean updateProduct(Integer id, ProductPostDto product) {
        var entity = productRepository.findById(id).orElseThrow();

        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setAmount(product.getAmount());
        entity.setPrice(product.getPrice());

        var category = new CategoryEntity();
        category.setId(product.getCategoryId());
        entity.setCategory(category);
        productRepository.save(entity);

        // Отримуємо список старих зображень у базі
        Map<String, ProductImageEntity> existingImages = entity.getImages().stream()
                .collect(Collectors.toMap(ProductImageEntity::getName, img -> img));

        List<ProductImageEntity> updatedImages = new ArrayList<>();
        var productFilesCount = product.getImageFiles() == null ? 0 : product.getImageFiles().size();

        for (int i = 0; i < productFilesCount; i++) {
            var img = product.getImageFiles().get(i);

            if ("old-image".equals(img.getContentType())) {
                // Оновлення пріоритету старого зображення
                var imageName = img.getOriginalFilename();
                if (existingImages.containsKey(imageName)) {
                    var oldImage = existingImages.get(imageName);
                    oldImage.setPriority(i + 1);
                    productImageRepository.save(oldImage);
                    updatedImages.add(oldImage);
                }
            } else {
                // Додавання нового зображення
                var imageName = fileService.load(img);
                var newImage = new ProductImageEntity();
                newImage.setName(imageName);
                newImage.setPriority(i + 1);
                newImage.setProduct(entity);
                productImageRepository.save(newImage);
            }
        }
        List<Integer> removeIds = new ArrayList<>();
        // Видалення зображень, яких немає в оновленому списку
        for (var img : entity.getImages()) {
            if (!updatedImages.contains(img)) {
                fileService.remove(img.getName());
                removeIds.add(img.getId());
            }
        }
        productImageRepository.deleteAllByIdInBatch(removeIds);
        return true;
    }

    public boolean deleteProduct(Integer id) {
        var res = productRepository.findById(id);
        if (res.isEmpty()) {
            return false;
        }
        var entity = res.get();

        //delete images
        var productImageEntities = entity.getImages();
        for (var productImage : productImageEntities) {
            fileService.remove(productImage.getName());
        }

        //delete product
        productRepository.deleteById(id);
        return true;
    }
}
