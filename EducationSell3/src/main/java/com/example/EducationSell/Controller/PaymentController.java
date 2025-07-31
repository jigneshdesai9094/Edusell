package com.example.EducationSell.Controller;

import com.example.EducationSell.DTO.PaymentStatusDTO;
import com.example.EducationSell.DTO.PaymentUserDetailDTO;
import com.example.EducationSell.Model.Payment;
import com.example.EducationSell.Services.Learner.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@Valid @RequestBody PaymentUserDetailDTO paymentDTO) throws RazorpayException {
        Order order = paymentService.createOrder(paymentDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("id", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", order.get("currency"));
        response.put("receipt", order.get("receipt"));
        response.put("status", order.get("status"));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(@Valid @RequestBody PaymentStatusDTO paymentStatusDTO) {
        Payment payment = paymentService.updateStatus(paymentStatusDTO, 2);
        return ResponseEntity.ok(payment);
    }
}