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
import java.util.List;

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
        var res = productRepository.findById(id);
        if (res.isEmpty()) {
            return false;
        }
        var entity = res.get();
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setAmount(product.getAmount());
        entity.setPrice(product.getPrice());

        var newCategoryId = product.getCategoryId();
        if (newCategoryId != entity.getCategory().getId() && categoryRepository.existsById(newCategoryId)){
            var category = new CategoryEntity();
            category.setId(newCategoryId);
            entity.setCategory(category);
        }
        productRepository.save(entity);

        //remove old images
        var oldProductImageEntities = entity.getImages();
        for (var productImage : oldProductImageEntities) {
            fileService.remove(productImage.getName());
            productImageRepository.delete(productImage);
        }

        //save new images
        var newImageFiles = product.getImageFiles();
        if (!newImageFiles.isEmpty()){
            var priority = 1;
            for (var file : newImageFiles) {
                if (file == null || file.isEmpty()) continue;
                var imageName = fileService.load(file);
                var img = new ProductImageEntity();
                img.setPriority(priority++);
                img.setName(imageName);
                img.setProduct(entity);
                productImageRepository.save(img);
            }
        }
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
