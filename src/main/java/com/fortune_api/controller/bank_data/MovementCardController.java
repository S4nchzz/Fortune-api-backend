package com.fortune_api.controller.bank_data;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.bank_data.AccountEntity;
import com.fortune_api.db.entities.bank_data.MovementCardEntity;
import com.fortune_api.db.services.bank_data.AccountService;
import com.fortune_api.db.services.bank_data.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/b_operations/movement")
public class MovementCardController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/findMovements")
    public ResponseEntity<?> findMovements(@RequestBody String uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        AccountEntity userAccount = accountService.findAccountByProprietary(user.getId());
        List<MovementCardEntity> movementCardEntityList = new ArrayList<>();

        userAccount.getCards().forEach(card -> {
            if (card.getCard_uuid().equals(uuid)) {
                movementCardEntityList.addAll(card.getMovements());
            }
        });

        return ResponseEntity.ok(movementCardEntityList);
    }
}
