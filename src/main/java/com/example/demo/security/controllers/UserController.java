package com.example.demo.security.controllers;

import com.example.demo.security.dto.UserDTO;
import com.example.demo.security.model.UserEntity;
import com.example.demo.security.services.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserController {

    @Autowired
    UserService userService;

    //only admins
    //read all
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Object> listUsers(){
        List<UserEntity> listUsers = userService.listAllUsers();
        List<UserDTO> listUsersDTO = listUsers.stream()
                .map( this::convertUserEntityToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listUsersDTO);
    }

    //create::register-DONE

    //read by id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> readUser(@PathVariable(value = "id") Long id){
        UserEntity userEntity = userService.readUser(id);
        if (userEntity == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.convertUserEntityToDTO(userEntity));
    }

    //update
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") Long id, @RequestBody UserDTO userDTO){
        if(!userService.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
        userDTO.setId(id);
        UserEntity userUpdate = userService.updateUser(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(this.convertUserEntityToDTO(userUpdate));
    }

    //delete
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Long id){
        if(!userService.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        UserEntity userDelete = userService.deleteUser(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(this.convertUserEntityToDTO(userDelete));
    }

    //convert
    private UserDTO convertUserEntityToDTO(UserEntity userEntity){
        Set<String> roles = userEntity.getRoles().stream()
                        .map( value -> value.getRoleName().toString())
                        .collect(Collectors.toSet());
        return UserDTO.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .roles(roles)
                .build();
    }

}
