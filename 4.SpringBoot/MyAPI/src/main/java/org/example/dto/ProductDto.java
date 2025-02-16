package org.example.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime creationTime;
    private String image;
    private float price;
    private Integer amount;
    private Integer categoryId;
    private String categoryName;
}
