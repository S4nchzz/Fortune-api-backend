package com.fortune_api.controller.user;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.entities.UserProfileEntity;
import com.fortune_api.db.services.UProfileService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userprofile")
public class UProfileController {
    @Autowired
    private UProfileService userProfileService;

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