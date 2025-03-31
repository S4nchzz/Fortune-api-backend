package com.fortune_api.controller.user;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.UserProfileEntity;
import com.fortune_api.db.services.UProfileService;
import com.fortune_api.db.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UProfileService userProfileService;

    @Autowired
    private UserService userService;

    @PostMapping("/createDigitalSign")
    public ResponseEntity<?> createDigitalSign(@RequestParam(name = "digital_sign") final int pin) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        UserEntity userEntity = userService.findUserById(user.getId());

        if (userEntity.getDigital_sign() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        userEntity.setDigital_sign(pin);
        userService.save(userEntity);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/findProfile")
    public UserProfileEntity findProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        return userProfileService.findProfileByUserId(user.getId());
    }
}