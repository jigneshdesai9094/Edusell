package com.example.EducationSell.Services.Learner;

import com.example.EducationSell.DTO.PaymentStatusDTO;
import com.example.EducationSell.DTO.PaymentUserDetailDTO;
import com.example.EducationSell.Model.Course;
import com.example.EducationSell.Model.Payment;
import com.example.EducationSell.Model.User;
import com.example.EducationSell.Repository.PaymentRepository;
import com.example.EducationSell.Services.Insteractor.CourseServices;
import com.example.EducationSell.Services.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EnrollementService enrollmentService;

    @Autowired
    private CourseServices courseServices;

    @Autowired
    private UserService userService;

    private final RazorpayClient razorpayClient;

    public PaymentService() throws RazorpayException {
        this.razorpayClient = new RazorpayClient("rzp_test_BxyHjA3pFoU5wz", "aebmlAh7u7Q4DEfZT25PWY4N");
    }

    @Transactional
    public Order createOrder(PaymentUserDetailDTO paymentDTO) throws RazorpayException {
        if (paymentDTO == null || paymentDTO.getAmount() == null || paymentDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("Invalid payment details");
        }

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", paymentDTO.getAmount());
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = razorpayClient.orders.create(orderRequest);

        Payment payment = new Payment();
        payment.setAmount(paymentDTO.getAmount());
        payment.setCustomerEmail(paymentDTO.getEmail());
        payment.setCustomerContact(paymentDTO.getContact());
        payment.setCustomerName(paymentDTO.getName());
        payment.setOrderId(order.get("id"));
        payment.setReceipt(order.get("receipt"));
        payment.setStatus(order.get("status"));
        payment.setPaymentId("test");

        paymentRepository.save(payment);
        return order;
    }

    @Transactional
    public Payment updateStatus(PaymentStatusDTO paymentStatusDTO, Integer userId) {
        if (paymentStatusDTO == null || paymentStatusDTO.getOrderId() == null) {
            throw new IllegalArgumentException("Invalid payment status details");
        }

        Payment payment = paymentRepository.findByOrderId(paymentStatusDTO.getOrderId());
        if (payment == null) {
            throw new IllegalArgumentException("Payment with order ID " + paymentStatusDTO.getOrderId() + " not found");
        }

        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        Course course = courseServices.findById(paymentStatusDTO.getCourseId());
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + paymentStatusDTO.getCourseId() + " not found");
        }

        payment.setStatus(paymentStatusDTO.getStatus());
        payment.setPaymentId(paymentStatusDTO.getPaymentId());

        enrollmentService.saveEnrollement(user, course, payment);

        return paymentRepository.save(payment);
    }
}