package org.example;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config {
    private String host;
    private String username;
    private String password;
    private String nameDb;
}
