package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.config.security.JwtService;
import org.example.dto.user.UserRegisterDto;
import org.example.entities.RoleEntity;
import org.example.entities.UserEntity;
import org.example.entities.UserRoleEntity;
import org.example.repository.IRoleRepository;
import org.example.repository.IUserRepository;
import org.example.repository.IUserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IUserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserEntity registerUser(UserRegisterDto user) throws Exception {
        var username = user.getUsername();
        var password = user.getPassword();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("Username '" + username + "' already exists!");
        }
        if (username == null || username.length() < 3) {
            throw new Exception("Username is less than 3 characters!");
        }
        if (password == null || password.length() < 6) {
            throw new Exception("Password is less than 6 characters!");
        }

        var userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity = userRepository.save(userEntity);

        RoleEntity userRole = roleRepository.findByName("USER").orElseThrow();
        UserRoleEntity ur = new UserRoleEntity(null, userEntity, userRole);
        userRoleRepository.save(ur);
        return userEntity;
    }

    public String authenticateUser(String username, String password) {
        var res = userRepository.findByUsername(username);
        if (res.isEmpty()) {
            return null;
        }
        var user = res.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        return jwtService.generateAccessToken(user);
    }
}
