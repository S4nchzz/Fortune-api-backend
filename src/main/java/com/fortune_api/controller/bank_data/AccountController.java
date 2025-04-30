package com.fortune_api.controller.bank_data;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.bank_data.AccountEntity;
import com.fortune_api.db.entities.bank_data.CardEntity;
import com.fortune_api.db.services.UserService;
import com.fortune_api.db.services.bank_data.AccountService;
import com.fortune_api.db.services.bank_data.CardService;
import org.json.HTTP;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/b_operations/account")
public class AccountController {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardService cardService;

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        String accountUUID = String.valueOf(UUID.randomUUID());
        accountUUID = accountUUID.substring(accountUUID.length() - 20).toUpperCase();

        final UserEntity proprietary = userService.findUserById(user.getId());

        AccountEntity newAccount = new AccountEntity(accountUUID, proprietary, 0.0);
        AccountEntity accountInDB = accountService.saveAccount(newAccount);

        final String cardNumber = generateCardNumber();
        final LocalDate currentDate = LocalDate.now();
        final String exp_date = currentDate.getMonthValue() + "/" + (currentDate.getYear() + 5);
        final int cvv = new Random().nextInt(900) + 100;
        final int pin = new Random().nextInt(9000) + 1000;

        CardEntity mainCard = new CardEntity(UUID.randomUUID().toString(), "MAIN", cardNumber, exp_date, cvv, pin, 0.0, false, accountInDB);
        cardService.saveCard(mainCard);

        accountInDB.getCards().add(mainCard);
        AccountEntity accountEntity = accountService.saveAccount(accountInDB);

        if (accountEntity != null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Primer dígito diferente de 0
        sb.append(random.nextInt(9) + 1);

        // Resto de los 15 dígitos
        for (int i = 0; i < 15; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    @GetMapping("/findAccount")
    public AccountEntity findAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        return accountService.findAccountByProprietary(user.getId());
    }
}
