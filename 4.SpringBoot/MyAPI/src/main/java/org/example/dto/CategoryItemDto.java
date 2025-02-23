package org.example.dto;

import lombok.Data;

@Data
public class CategoryItemDto {
    private Integer id;
    private String name;
    private String image;
    private String description;
    private String dateCreated;
}
