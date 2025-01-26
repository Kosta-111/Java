package org.example.seeder;

import org.example.entites.ProductEntity;
import org.example.repository.ICategoryRepository;
import org.example.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

@Component
public class ProductsSeeder {

    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ICategoryRepository categoryRepository;

    public void seed() {
        if(productRepository.count() == 0) {
            var categories = categoryRepository.findAll();
            var random = new Random();

            var product1 = new ProductEntity();
            product1.setName("IPhone 16");
            product1.setDescription("8/256");
            product1.setImage("iphone16.jpg");
            product1.setCreationTime(LocalDateTime.now());
            product1.setAmount(5);
            product1.setPrice(1000);
            product1.setCategory(categories.get(random.nextInt(categories.size())));

            var product2 = new ProductEntity();
            product2.setName("Jacket");
            product2.setDescription("very nice");
            product2.setImage("jacket.jpg");
            product2.setCreationTime(LocalDateTime.now());
            product2.setAmount(12);
            product2.setPrice(150);
            product2.setCategory(categories.get(random.nextInt(categories.size())));

            var product3 = new ProductEntity();
            product3.setName("C++");
            product3.setDescription("good book");
            product3.setImage("cpp_book.jpg");
            product3.setCreationTime(LocalDateTime.now());
            product3.setAmount(7);
            product3.setPrice(20);
            product3.setCategory(categories.get(random.nextInt(categories.size())));

            // Зберігаємо дані до бази
            productRepository.saveAll(Arrays.asList(product1, product2, product3));
        }
    }
}
