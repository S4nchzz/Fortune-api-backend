package com.fortune_api.controller.bizum;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.UserProfileEntity;
import com.fortune_api.db.entities.bank_data.AccountEntity;
import com.fortune_api.db.entities.bank_data.CardEntity;
import com.fortune_api.db.entities.bizum.BizumEntity;
import com.fortune_api.db.services.UProfileService;
import com.fortune_api.db.services.UserService;
import com.fortune_api.db.services.bank_data.AccountService;
import com.fortune_api.db.services.bank_data.CardService;
import com.fortune_api.db.services.bizum.BizumService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/b_operations/bizum")
public class BizumController {
    @Autowired
    private UserService userService;

    @Autowired
    private UProfileService uProfileService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardService cardService;

    @Autowired
    private BizumService bizumService;

    @PostMapping("/makeBizum")
    public ResponseEntity<?> makeBizum(@RequestBody String body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        JSONObject bizumJSON = new JSONObject(body);
        final double amount = bizumJSON.getDouble("amount");
        final String phone = bizumJSON.getString("phone");
        final String description = bizumJSON.getString("description");

        UserProfileEntity userProfileToSend = uProfileService.findByPhone(phone);
        if (userProfileToSend == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UserProfileEntity requestingUserProfile = uProfileService.findProfileByUserId(user.getId());
        if (requestingUserProfile != null && userProfileToSend.getPhone().equals(requestingUserProfile.getPhone())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UserEntity userToSend = userService.findUserById(userProfileToSend.getUserId());

        if (userToSend == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        AccountEntity accountToSend = accountService.findAccountByProprietary(userToSend.getId());
        if (accountToSend == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        for (CardEntity cardEntity : accountToSend.getCards()) {
            if (cardEntity.getCardType().equalsIgnoreCase("MAIN")) {
                AccountEntity requestingUserAccount = accountService.findAccountByProprietary(user.getId());
                if (requestingUserAccount == null) {
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
                }

                cardEntity.setBalance(cardEntity.getBalance() + amount);
                cardService.saveCard(cardEntity);

                CardEntity requestingUserMainCard = requestingUserAccount.getCards()
                        .stream().filter( f -> f.getCardType().equalsIgnoreCase("MAIN"))
                        .findFirst()
                        .orElse(null);

                if (requestingUserMainCard == null) {
                    cardEntity.setBalance(cardEntity.getBalance() - amount);
                    cardService.saveCard(cardEntity);
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
                }

                requestingUserMainCard.setBalance(requestingUserMainCard.getBalance() - amount);
                cardService.saveCard(requestingUserMainCard);

                BizumEntity bizum = bizumService.saveOperation(user, userToSend, description, amount);

                if (bizum == null) {
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
                }

                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }
}
