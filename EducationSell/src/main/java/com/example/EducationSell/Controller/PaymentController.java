//package com.example.EducationSell.Controller;
//
//import com.razorpay.Order;
//import com.razorpay.RazorpayClient;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class PaymentController {
//
//    private final RazorpayClient razorpayClient;
//
//    public PaymentController() throws Exception {
//        this.razorpayClient = new RazorpayClient("rzp_test_BxyHjA3pFoU5wz", "aebmlAh7u7Q4DEfZT25PWY4N");
//    }
//
//    @PostMapping("/create-order")
//    public String createOrder(String id) throws Exception {
//        JSONObject orderJson = new JSONObject();
//        orderJson.put("amount", 5* 100); // Amount in paise
//        orderJson.put("currency", "INR");
//
//
//        JSONObject transfer = new JSONObject();
//        transfer.put("account", id); // Replace with actual test linkedAccountId
//        transfer.put("amount", (long) (5 * 100 * 0.8)); // 80% to instructor
//        transfer.put("currency", "INR");
//        orderJson.put("transfers", new JSONArray().put(transfer));
//
//        Order order = razorpayClient.orders.create(orderJson);
//        return order.toString();
//    }
//    @PostMapping("/add-linked-account")
//    public String addLinkedAccount() throws Exception {
//        JSONObject linkedAccountJson = new JSONObject();
//        linkedAccountJson.put("name","demo_account_holderName");
//        linkedAccountJson.put("email", "test.instructor@example.com");
//        linkedAccountJson.put("contact", "9999999999");
//        linkedAccountJson.put("type", "vendor");
//
//        JSONObject bankAccount = new JSONObject();
//        bankAccount.put("account_number", "9876543210"); // Test account number
//        bankAccount.put("ifsc", "RAZR0000001"); // Test IFSC
//        bankAccount.put("name", "demo_account_holderName");
//        linkedAccountJson.put("bank_account", bankAccount);
//
//
//        try {
//            JSONObject response = razorpayClient.account.create(linkedAccountJson).toJson();
//            return response.get("id").toString();
//        } catch (Exception e) {
//            throw new Exception("Failed to create linked account: " + e.getMessage());
//        }
//
//        // Save to InstructorBankAccount entity
////        return response.get("id").toString();
//    }
//}
