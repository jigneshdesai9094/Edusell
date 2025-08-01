package com.example.EducationSell.Repository;

import com.example.EducationSell.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    public Payment findByOrderId(String orderId);
}
