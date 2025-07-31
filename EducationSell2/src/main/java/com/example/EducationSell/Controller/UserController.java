package com.example.EducationSell.Controller;


import com.example.EducationSell.DTO.UserRegisterDTO;
import com.example.EducationSell.DTO.VerificationDTO;
import com.example.EducationSell.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController
{

    @Autowired
    private UserService userService;


        @PostMapping("/register")
        public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO dto) {
            try {
                return new ResponseEntity<>(userService.register(dto), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        }

            @PostMapping("/verify")
            public ResponseEntity<?> verify(@Valid @RequestBody VerificationDTO verificationDto)
            {
                System.out.println("call this verify register controller methods ---------");
                System.out.println(verificationDto.getEmail() + " "+verificationDto.getOtp());
                try
                {
                    return  new ResponseEntity<>(userService.verifyOtp(verificationDto), HttpStatus.OK);
                }
                catch (Exception e)
                {
                    return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
                }

    }
}
