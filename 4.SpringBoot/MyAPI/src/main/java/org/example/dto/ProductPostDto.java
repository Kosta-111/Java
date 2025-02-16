package org.example.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductPostDto {
    private String name;
    private String description;
    private MultipartFile imageFile;
    private float price;
    private Integer amount;
    private Integer categoryId;
}
