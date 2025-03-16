package org.example.controller;

import org.example.dto.user.UserLoginDto;
import org.example.dto.user.UserRegisterDto;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/register", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> register(@ModelAttribute UserRegisterDto user) {
        try {
            var res = userService.registerUser(user);
            return new ResponseEntity<>("User '" + res.getUsername() + "' registered!", HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/login", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> login(@ModelAttribute UserLoginDto user) {
        var accessToken = userService.authenticateUser(user.getUsername(), user.getPassword());
        return accessToken != null
                ? new ResponseEntity<>(accessToken, HttpStatus.OK)
                : new ResponseEntity<>("Invalid login or password!", HttpStatus.BAD_REQUEST);
    }
}
