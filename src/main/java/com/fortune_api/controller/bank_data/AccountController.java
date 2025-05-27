package com.fortune_api.controller.bank_data;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.bank_data.AccountEntity;
import com.fortune_api.db.entities.bank_data.CardEntity;
import com.fortune_api.db.entities.bank_data.MovementCardEntity;
import com.fortune_api.db.entities.bizum.BizumEntity;
import com.fortune_api.db.services.UProfileService;
import com.fortune_api.db.services.UserService;
import com.fortune_api.db.services.bank_data.AccountService;
import com.fortune_api.db.services.bank_data.CardService;
import com.fortune_api.db.services.bizum.BizumService;
import com.fortune_api.network.response.FastContactResponse;
import org.apache.coyote.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/b_operations/account")
public class AccountController {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardService cardService;

    @Autowired
    private BizumService bizumService;

    @Autowired
    private UProfileService uProfileService;

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
    public ResponseEntity<?> findAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        AccountEntity accountEntity = accountService.findAccountByProprietary(user.getId());
        double cardSumBalance = 0.0;

        for (CardEntity c : accountEntity.getCards()) {
            cardSumBalance += c.getBalance();
        }


        return ResponseEntity.ok(
                new JSONObject()
                        .put("account_id", accountEntity.getAccount_id())
                        .put("total_balance", cardSumBalance)
                        .toString()
        );
    }

    @GetMapping("/getAccountBalance")
    public ResponseEntity<?> getAccountBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        AccountEntity accountEntity = accountService.findAccountByProprietary(user.getId());
        if (accountEntity == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        double accountBalance = 0.0;
        for (CardEntity c : accountEntity.getCards()) {
            accountBalance += c.getBalance();
        }

        return ResponseEntity.ok(
                new JSONObject()
                        .put("accountBalance", accountBalance)
                        .toString()
        );
    }

    @GetMapping("/addNewCard")
    public ResponseEntity<?> createCard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        AccountEntity accountEntity = accountService.findAccountByProprietary(user.getId());
        if (accountEntity == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        final String cardNumber = generateCardNumber();
        final LocalDate currentDate = LocalDate.now();
        final String exp_date = currentDate.getMonthValue() + "/" + (currentDate.getYear() + 5);
        final int cvv = new Random().nextInt(900) + 100;
        final int pin = new Random().nextInt(9000) + 1000;

        CardEntity prepaidCard = new CardEntity(UUID.randomUUID().toString(), "PREPAID", cardNumber, exp_date, cvv, pin, 0.0, false, accountEntity);
        CardEntity cardFromDB = cardService.saveCard(prepaidCard);

        if (cardFromDB != null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    @GetMapping("/getAccountData")
    public ResponseEntity<?> getAccountData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        AccountEntity accountEntity = accountService.findAccountByProprietary(user.getId());
        if (accountEntity == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        double amount = 0.0;
        for (CardEntity c : accountEntity.getCards()) {
            amount += c.getBalance();
        }


        return ResponseEntity.ok(
                new JSONObject()
                        .put("accountID", accountEntity.getAccount_id())
                        .put("accountBalance", amount)
                        .toString()
        );
    }

    @GetMapping("/getAccountMovements")
    public ResponseEntity<?> getAccountMovement() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        AccountEntity accountEntity = accountService.findAccountByProprietary(user.getId());
        if (accountEntity == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        List<MovementCardEntity> movements = new ArrayList<>();
        for (CardEntity c : accountEntity.getCards()) {
            movements.addAll(c.getMovements());
        }

        return ResponseEntity.ok(movements);
    }

    @GetMapping("/getFastContacts")
    public ResponseEntity<?> getFastContacts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        AccountEntity accountEntity = accountService.findAccountByProprietary(user.getId());
        if (accountEntity == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        final List<FastContactResponse> fastContactResponseList = new ArrayList<>();

        for (BizumEntity b : this.bizumService.getMoreThan3Bizums(user.getId())) {
            final String name = this.uProfileService.findProfileByUserId(b.getTo().getId()).getName();
            String formattedName = name;

            if (name.split(" ").length > 1) {
                formattedName = name.split(" ")[0] + " " + name.split(" ")[1].charAt(0) + ".";
            }

            fastContactResponseList.add(new FastContactResponse(
                this.uProfileService.findProfileByUserId(b.getTo().getId()).getPfp(),
                formattedName,
                b.getTo().getId()
            ));
        }

        deleteDupedEntities(fastContactResponseList);

        return ResponseEntity.ok(fastContactResponseList);
    }

    private void deleteDupedEntities(List<FastContactResponse> list) {
        Set<Long> seen = new HashSet<>();
        list.removeIf(item -> !seen.add(item.getTo_id()));
    }
}