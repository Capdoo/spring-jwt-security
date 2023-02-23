package com.example.demo.security.services;

import com.example.demo.security.dto.UserDTO;
import com.example.demo.security.model.UserEntity;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface UserService {

    public List<UserEntity> listAllUsers();
    public UserEntity createUser(UserDTO userDTO);
    public UserEntity readUser(Long id);
    public UserEntity updateUser(UserDTO userDTO);
    public UserEntity deleteUser(UserDTO userDTO);
    public Boolean existsById(Long id);

    //
    public UserEntity readByUsernameOrEmail(String usernameOrEmail);
    public UserEntity readByTokenPassword(String tokenPassword);

    //
    public Boolean existsByUsername(String username);
    public Boolean existsByUsernameOrEmail(String usernameOrEmail);
    public Boolean existsByEmail(String email);


}
