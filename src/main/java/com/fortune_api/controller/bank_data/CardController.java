package com.fortune_api.controller.bank_data;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.bank_data.AccountEntity;
import com.fortune_api.db.entities.bank_data.CardEntity;
import com.fortune_api.db.entities.bank_data.MovementCardEntity;
import com.fortune_api.db.services.bank_data.AccountService;
import com.fortune_api.db.services.bank_data.CardService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/b_operations/card")
public class CardController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private CardService cardService;

    @GetMapping("/findMainCard")
    public ResponseEntity<?> getMainCard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        AccountEntity account = accountService.findAccountByProprietary(user.getId());

        return ResponseEntity.ok(account.getCards().stream()
                .filter(card -> "MAIN".equals(card.getCardType()))
                .findFirst()
                .orElse(null));
    }

    @GetMapping("/findCards")
    public ResponseEntity<List<CardEntity>> findCards() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        AccountEntity account = accountService.findAccountByProprietary(user.getId());
        return ResponseEntity.ok(account.getCards().stream().toList());
    }

    @PostMapping("/manageCardLock")
    public ResponseEntity<?> findMovements(@RequestBody String cardUUID) {
        JSONObject json = new JSONObject(cardUUID);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        AccountEntity userAccount = accountService.findAccountByProprietary(user.getId());

        boolean cardLockState = false;

        for(CardEntity c : userAccount.getCards()) {
            if (c.getCard_uuid().equals(json.get("card_uuid"))) {
                c.setBlocked(!c.isBlocked());
                cardLockState = c.isBlocked();

                cardService.saveCard(c);
            }
        }

        return ResponseEntity.ok(new JSONObject()
                .put("locked", cardLockState)
                .toString()
        );
    }

    @PostMapping("/isCardLocked")
    public ResponseEntity<?> isCardLocked(@RequestBody String cardUUID) {
        JSONObject json = new JSONObject(cardUUID);

        CardEntity cardEntity = getCard((String) json.get("card_uuid"));

        if (cardEntity != null) {
            return ResponseEntity.ok(new JSONObject()
                    .put("locked", cardEntity.isBlocked())
                    .toString()
            );
        } else {
            return ResponseEntity.ok(new JSONObject()
                    .put("locked", false)
                    .toString()
            );
        }
    }

    @PostMapping("/getCardNumber")
    public ResponseEntity<?> getCardNumber(@RequestBody String cardUUID) {
        JSONObject json = new JSONObject(cardUUID);

        CardEntity cardEntity = getCard((String) json.get("card_uuid"));

        if (cardEntity != null) {
            return ResponseEntity.ok(new JSONObject()
                    .put("cardNumber", cardEntity.getCardNumber())
                    .toString()
            );
        } else {
            return ResponseEntity.ok(new JSONObject()
                    .put("cardNumber", "???? ???? ???? ????")
                    .toString()
            );
        }
    }

    private CardEntity getCard(String cardUUID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        AccountEntity userAccount = accountService.findAccountByProprietary(user.getId());

        for(CardEntity c : userAccount.getCards()) {
            if (c.getCard_uuid().equals(cardUUID)) {
                return c;
            }
        }

        return null;
    }
}