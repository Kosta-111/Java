package org.example.service;

import org.example.dto.ProductDto;
import org.example.dto.ProductPostDto;
import org.example.entites.CategoryEntity;
import org.example.entites.ProductEntity;
import org.example.mapper.ProductMapper;
import org.example.repository.ICategoryRepository;
import org.example.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProductMapper productMapper;

    public List<ProductDto> getAllProducts() {
        var entities = productRepository.findAll();
        return productMapper.toDTO(entities);
    }

    public ProductDto getProductById(Integer id) {
        var res = productRepository.findById(id);
        return res.isPresent()
                ? productMapper.toDTO(res.get())
                : null;
    }

    public ProductDto createProduct(ProductPostDto product) {
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

        var newImageFile = product.getImageFile();
        if (newImageFile != null && !newImageFile.isEmpty()){
            var imagePath = fileService.load(newImageFile);
            entity.setImage(imagePath);
        }
        var res = productRepository.save(entity);
        return productMapper.toDTO(res);
    }

    public boolean updateProduct(Integer id, ProductPostDto product) {
        var res = productRepository.findById(id);
        if (res.isEmpty()){
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

        var newImageFile = product.getImageFile();
        if (newImageFile != null && !newImageFile.isEmpty()){
            var newImagePath = fileService.replace(entity.getImage(), newImageFile);
            entity.setImage(newImagePath);
        }
        productRepository.save(entity);
        return true;
    }

    public boolean deleteProduct(Integer id) {
        var res = productRepository.findById(id);
        if (res.isEmpty()){
            return false;
        }
        fileService.remove(res.get().getImage());
        productRepository.deleteById(id);
        return true;
    }
}
