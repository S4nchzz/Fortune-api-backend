package com.fortune_api.controller.user;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.UserProfileEntity;
import com.fortune_api.db.services.UProfileService;
import com.fortune_api.db.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserEntity createDigitalSign(@RequestParam(name = "digital_sign") final int pin) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        UserEntity userEntity = userService.findUserById(user.getId());

        if (userEntity.getDigital_sign() != null) {
            return null; // For some reason the user has already a digital sign
        }

        userEntity.setDigital_sign(pin);
        userService.save(userEntity);

        return userEntity;
    }

    @PostMapping("/generateProfile")
    public UserProfileEntity generateProfile(@RequestParam("name") final String name, @RequestParam(name = "address") final String address, @RequestParam(name = "phone") final String phone, @RequestParam(name = "online") final boolean online) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();

        return userProfileService.generateUserProfile(user.getId(), name, address, phone, online);
    }

    @GetMapping("/findUserByProfileId")
    public UserProfileEntity findProfileByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();


        return userProfileService.findProfileByUserId(user.getId());
    }
}