package com.example.EducationSell.Services.Insteractor;

import com.example.EducationSell.DTO.BankAccountDTO;
import com.example.EducationSell.Helper.mapper.BankAccountMapper;
import com.example.EducationSell.Model.BankAccount;
import com.example.EducationSell.Repository.BankAccountRepository;
import com.example.EducationSell.Services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private UserService userService;

    @Transactional
    public BankAccount addBankAccount(BankAccountDTO bankAccountDTO, Integer uid) {
        if (userService.findById(uid) == null) {
            throw new IllegalArgumentException("User with ID " + uid + " not found");
        }
        BankAccount bankAccount = bankAccountMapper.toEntity(bankAccountDTO);
        bankAccount.setInstructor(userService.findById(uid));
        return bankAccountRepository.save(bankAccount);
    }

    @Transactional
    public void deleteBankAccount(Integer bankAccountId) {
        if (!bankAccountRepository.existsById(bankAccountId)) {
            throw new IllegalArgumentException("Bank account with ID " + bankAccountId + " not found");
        }
        bankAccountRepository.deleteById(bankAccountId);
    }
}