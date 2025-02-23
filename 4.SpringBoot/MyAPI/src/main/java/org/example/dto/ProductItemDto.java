package org.example.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductItemDto {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime dateCreated;
    private float price;
    private Integer amount;
    private Integer categoryId;
    private String categoryName;
    private List<String> images;
}
