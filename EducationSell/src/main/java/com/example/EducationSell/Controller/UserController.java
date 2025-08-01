package com.example.EducationSell.Controller;


import com.example.EducationSell.DTO.UserDTO;
import com.example.EducationSell.DTO.UserRegisterDTO;
import com.example.EducationSell.DTO.VerificationDTO;
import com.example.EducationSell.Model.Role;
import com.example.EducationSell.Model.User;
import com.example.EducationSell.Repository.RoleRepository;
import com.example.EducationSell.Repository.UserRepository;
import com.example.EducationSell.Services.CustomUserDetailsService;
import com.example.EducationSell.Services.UserService;
import com.example.EducationSell.Utillity.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

        @GetMapping("/FindUser")
        public ResponseEntity<?> find(){
            String username = "Instructor";
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(("User not found with username: " + username));
            }
            User user = userOptional.get();
            UserDTO userDTO = new UserDTO(
                    user.getUsername(),
                    user.getEmail(),
                    user.getMobileNo(),
                    user.getCreatedAt(),
                    user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet())
            );
            return ResponseEntity.ok(userDTO);
        }


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

        @PostMapping("/login")
        public ResponseEntity<?> doLogin(@RequestBody User user){
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
                );
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
                if(userDetails == null){
                    System.out.println("not found");
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }else System.out.println("found");
                System.out.println(userDetails);
                String jwt = jwtUtils.generateToken(userDetails.getUsername());
                return new ResponseEntity<>(jwt,HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }
}
