package com.fortune_api.controller.bank_data;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.bank_data.AccountEntity;
import com.fortune_api.db.entities.bank_data.CardEntity;
import com.fortune_api.db.entities.bank_data.MovementCardEntity;
import com.fortune_api.db.services.UProfileService;
import com.fortune_api.db.services.bank_data.AccountService;
import com.fortune_api.db.services.bank_data.CardService;
import com.fortune_api.db.services.bank_data.MovementCardService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/b_operations/movement")
public class MovementCardController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private MovementCardService movementCardService;

    @Autowired
    private UProfileService uProfileService;

    @Autowired
    private CardService cardService;

    @PostMapping("/findMovements")
    public ResponseEntity<?> findMovements(@RequestBody String uuid) {
        JSONObject json = new JSONObject(uuid);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        AccountEntity userAccount = accountService.findAccountByProprietary(user.getId());
        List<MovementCardEntity> movementCardEntityList = new ArrayList<>();

        for(CardEntity c : userAccount.getCards()) {
            if (c.getCard_uuid().equals(json.get("card_uuid"))) {
                movementCardEntityList.addAll(c.getMovements());
            }
        }

        return ResponseEntity.ok(movementCardEntityList);
    }

    @PostMapping("/simulatePayment")
    public ResponseEntity<?> simulatePayment(@RequestBody String simulatePaymentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.ok(
                    new JSONObject()
                            .put("paymentSimulated", false)
                            .toString()
            );
        }

        JSONObject simulatePaymentRequestJSON = new JSONObject(simulatePaymentRequest);
        final String cardUUID = simulatePaymentRequestJSON.getString("cardUUID");
        final String amount = "-" + simulatePaymentRequestJSON.getDouble("amount");
        final String receptorEntity = simulatePaymentRequestJSON.getString("receptorEntity");

        final AccountEntity account = accountService.findAccountByProprietary(user.getId());

        boolean movementSaved = false;

        for (CardEntity cardEntity : account.getCards()) {
            if (cardEntity.getCard_uuid().equals(cardUUID)) {
                final MovementCardEntity movement = movementCardService.saveMovement(amount, receptorEntity, uProfileService.findProfileByUserId(user.getId()).getName(), cardEntity);
                if (movement != null) {
                    movementSaved = true;
                    cardEntity.setBalance(cardEntity.getBalance() - simulatePaymentRequestJSON.getDouble("amount"));
                    cardService.saveCard(cardEntity);
                }
            }
        }

        return ResponseEntity.ok(
                new JSONObject()
                        .put("paymentSimulated", movementSaved)
                        .toString()
        );
    }
}
