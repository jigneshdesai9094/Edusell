package com.example.EducationSell.Services;

import com.example.EducationSell.DTO.UserRegisterDTO;
import com.example.EducationSell.DTO.VerificationDTO;
import com.example.EducationSell.Model.Role;
import com.example.EducationSell.Model.User;
import com.example.EducationSell.Repository.RoleRepository;
import com.example.EducationSell.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailOtpService emailOtpService;

    @Transactional
    public User findById(Integer id) {
        return userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));
    }

    @Transactional
    public String register(UserRegisterDTO dto) {
        if (dto == null || dto.getEmail() == null || dto.getUsername() == null || dto.getMobileNo() == null) {
            throw new IllegalArgumentException("Invalid user registration details");
        }

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

    @Transactional
    public String verifyOtp(VerificationDTO verificationDTO) {
        UserRegisterDTO dto = emailOtpService.verifyOtp(verificationDTO);
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setUsername(dto.getUsername());
        user.setMobileNo(dto.getMobileNo());

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role with ID " + dto.getRoleId() + " not found"));

        user.getRoles().add(role);
        userRepository.save(user);

        return "register successfully";
    }

    public void save(User user) {
        userRepository.save(user);
    }
}