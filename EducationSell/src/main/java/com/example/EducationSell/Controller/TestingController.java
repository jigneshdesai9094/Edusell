package com.example.EducationSell.Controller;

import com.example.EducationSell.Model.User;
import com.example.EducationSell.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class TestingController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<?> getUser(){
        User user = userService.findById(1);
        return ResponseEntity.ok(user);
    }
}
