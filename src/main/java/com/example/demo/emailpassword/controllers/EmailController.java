package com.example.demo.emailpassword.controllers;

import com.example.demo.dto.MessageDTO;
import com.example.demo.emailpassword.dto.ChangePasswordDTO;
import com.example.demo.emailpassword.dto.EmailValuesDTO;
import com.example.demo.emailpassword.services.EmailService;
import com.example.demo.security.model.UserModel;
import com.example.demo.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/email-password")
@CrossOrigin
public class EmailController {

    @Autowired
    EmailService emailService;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final String emailSubject = "Change password";

    @PostMapping("/send-email")
    public ResponseEntity<Object> sendEmailTemplate(@RequestBody EmailValuesDTO emailValuesDTO){

        Optional<UserModel> usuarioModelOptional = userService.getByUsernameOrEmail(emailValuesDTO.getMailTo());
        if(!usuarioModelOptional.isPresent()){
            return new ResponseEntity<Object>(new MessageDTO("No user found with these credentials"), HttpStatus.NOT_FOUND);
        }
        UserModel usuarioModel = usuarioModelOptional.get();

        emailValuesDTO.setMailFrom(emailFrom);
        emailValuesDTO.setSubject(emailSubject);
        emailValuesDTO.setMailTo(usuarioModel.getEmail());
        emailValuesDTO.setUserName(usuarioModel.getUsername());

        UUID uuid = UUID.randomUUID();
        String tokenPassword = uuid.toString();
        emailValuesDTO.setToken(tokenPassword);
        usuarioModel.setTokenPassword(tokenPassword);
        userService.save(usuarioModel);

        emailService.sendEmailTemplate(emailValuesDTO);
        return new ResponseEntity<Object>(new MessageDTO("Email sent successfully"), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<Object>(new MessageDTO("Wrong fields"), HttpStatus.BAD_REQUEST);
        }
        if(!changePasswordDTO.getPassword().equals(changePasswordDTO.getConfirmPassword())){
            return new ResponseEntity<Object>(new MessageDTO("Passwords do not match"), HttpStatus.BAD_REQUEST);
        }

        Optional<UserModel> usuarioModelOptional = userService.getByTokenPassword(changePasswordDTO.getTokenPassword());
        if(!usuarioModelOptional.isPresent()){
            return new ResponseEntity<Object>(new MessageDTO("User not found"), HttpStatus.NOT_FOUND);
        }
        UserModel usuarioModel = usuarioModelOptional.get();
        String newPassword = passwordEncoder.encode(changePasswordDTO.getPassword());
        usuarioModel.setPassword(newPassword);
        usuarioModel.setTokenPassword(null);
        userService.save(usuarioModel);

        return new ResponseEntity<Object>(new MessageDTO("Password updated"), HttpStatus.OK);

    }
}
