package com.example.EducationSell.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Api/Admin")
public class AdminController {

    @GetMapping("/sayHii")
    public ResponseEntity<?> sayHii(){
        return new ResponseEntity<>("Hello ", HttpStatus.OK);
    }
}
