package com.example.EducationSell.Controller;

import com.example.EducationSell.DTO.BankAccountDTO;
import com.example.EducationSell.Model.BankAccount;
import com.example.EducationSell.Services.Insteractor.BankAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-account")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("/add-account")
    public ResponseEntity<?> addBankAccount(@Valid @RequestBody BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = bankAccountService.addBankAccount(bankAccountDTO, 1);
        return ResponseEntity.ok(bankAccount);
    }

    @DeleteMapping("/delete-account/{bankAccountId}")
    public ResponseEntity<?> deleteBankAccount(@PathVariable Integer bankAccountId) {
        bankAccountService.deleteBankAccount(bankAccountId);
        return ResponseEntity.ok("Bank account deleted successfully");
    }
}