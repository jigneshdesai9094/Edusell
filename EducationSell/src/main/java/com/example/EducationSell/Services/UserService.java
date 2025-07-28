package com.example.EducationSell.Services;


import com.example.EducationSell.DTO.UserRegisterDTO;
import com.example.EducationSell.DTO.VerificationDTO;
import com.example.EducationSell.Model.Role;
import com.example.EducationSell.Model.User;
import com.example.EducationSell.Repository.RoleRepository;
import com.example.EducationSell.Repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private RoleRepository roleRepository;

     @Autowired
     private EmailOtpService emailOtpService;

    @Transactional(readOnly = true)
    public User findById(Integer id) {
        return userRepository.findByIdWithRoles(id).orElse(null);
    }

     public String register(UserRegisterDTO dto)
     {


         // Validate uniqueness of email and username (since no UNIQUE constraints)
         if (userRepository.existsByEmail(dto.getEmail())) {
             throw new IllegalArgumentException("Email already exists");
         }
         if (userRepository.existsByUsername(dto.getUsername())) {
             throw new IllegalArgumentException("Username already exists");
         }

         if (userRepository.existsByMobileNo(dto.getMobileNo())) {
             throw new IllegalArgumentException("Mobile number already exists");
         }

         emailOtpService.sendOtp(dto);
         return "OTP sent to " + dto.getEmail();


     }

     public String verifyOtp(VerificationDTO verificationDTO) throws Exception {

         UserRegisterDTO dto = emailOtpService.verifyOtp(verificationDTO);
         System.out.println("user service dto check");
         System.out.println(dto);
         User user = new User();
         user.setEmail(dto.getEmail());
         user.setPassword(dto.getPassword());
         user.setUsername(dto.getUsername());
         user.setMobileNo(dto.getMobileNo());

         Optional<Role> optionalRole = roleRepository.findById(dto.getRoleId());

         Role role = optionalRole.get();

//         role.getUsers().add(user);

         user.getRoles().add(role);

//         roleRepository.save(role);

         userRepository.save(user);

         return  "register successfully";
     }

}
