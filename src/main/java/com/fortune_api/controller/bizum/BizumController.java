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
import com.fortune_api.network.response.BizumResponse;
import jakarta.persistence.Entity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

                BizumEntity bizum = bizumService.saveOperation(user, userToSend, description, amount, false);

                if (bizum == null) {
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
                }

                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    @PostMapping("/getBizums")
    public ResponseEntity<?> getBizums() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        List<BizumResponse> bizumResponse = new ArrayList<>();
        List<BizumEntity> bizums = bizumService.getNonRequestedBizums(user.getId());

        for (BizumEntity b : bizums) {
            UserProfileEntity fromProfile = null;

            if (b.getTo().getId() != user.getId() && b.getFrom().getId() == user.getId()) {
                fromProfile = uProfileService.findProfileByUserId(b.getTo().getId());
            } else if (b.getTo().getId() == user.getId() && b.getFrom().getId() != user.getId()) {
                fromProfile = uProfileService.findProfileByUserId(b.getFrom().getId());
            }

            if (fromProfile != null) {
                boolean amountIn = false;
                if (b.getFrom().getId() != user.getId()) {
                    amountIn = true;
                }

                bizumResponse.add(new BizumResponse(
                        b.getDate(),
                        getFormattedName(fromProfile.getName()),
                        b.getAmount(),
                        b.getDescription(),
                        amountIn
                ));
            }
        }

        return ResponseEntity.ok(bizumResponse);
    }

    @PostMapping("/getRequestedBizums")
    public ResponseEntity<?> getRequestedBizums() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        List<BizumResponse> bizumResponse = new ArrayList<>();
        List<BizumEntity> bizums = bizumService.getRequestedBizums(user.getId());

        for (BizumEntity b : bizums) {
            UserProfileEntity fromProfile = null;

            if (b.getTo().getId() != user.getId() && b.getFrom().getId() == user.getId()) {
                fromProfile = uProfileService.findProfileByUserId(b.getTo().getId());
            } else if (b.getTo().getId() == user.getId() && b.getFrom().getId() != user.getId()) {
                fromProfile = uProfileService.findProfileByUserId(b.getFrom().getId());
            }

            if (fromProfile != null) {
                boolean amountIn = false;
                if (b.getFrom().getId() != user.getId()) {
                    amountIn = true;
                }

                bizumResponse.add(new BizumResponse(
                        b.getDate(),
                        getFormattedName(fromProfile.getName()),
                        b.getAmount(),
                        b.getDescription(),
                        amountIn
                ));
            }
        }

        return ResponseEntity.ok(bizumResponse);
    }

    @PostMapping("/requestBizum")
    public ResponseEntity<?> requestBizum(@RequestBody String body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        JSONObject requestBizumJSON = new JSONObject(body);
        final double amount = requestBizumJSON.getDouble("amount");
        final String phone = requestBizumJSON.getString("phone");
        final String description = requestBizumJSON.getString("description");

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

        BizumEntity b = bizumService.saveOperation(user, userToSend, description, amount, true);

        if (b != null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    private String getFormattedName(final String name) {
        String [] splitedName = name.split(" ");
        String formattedName = splitedName[0];

        if (splitedName.length > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(formattedName).append(" ");
            sb.append(splitedName[1].toUpperCase().charAt(0)).append(".");
            formattedName = sb.toString();
        }

        return formattedName;
    }
}
