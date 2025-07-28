package com.example.EducationSell.Repository;

import com.example.EducationSell.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByMobileNo(String mobileNo);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.userId = :id")
    Optional<User> findByIdWithRoles(@Param("id") Integer id);
}
