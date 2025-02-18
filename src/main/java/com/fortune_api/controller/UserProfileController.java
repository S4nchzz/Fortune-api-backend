package com.fortune_api.controller;

import com.fortune_api.db.entities.UserProfileEntity;
import com.fortune_api.db.services.UserProfileService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userprofile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/generateProfile")
    public String generateProfile(@RequestParam("user_id") final long user_id, @RequestParam("name") final String name, @RequestParam(name = "address") final String address, @RequestParam(name = "telf") final String telf) {

        final UserProfileEntity userProfileEntity = userProfileService.generateUserProfile(user_id, name, address, telf);

        return new JSONObject()
                .put("user_id", userProfileEntity.getUser().getId())
                .put("online", userProfileEntity.isOnline())
                .put("name", userProfileEntity.getName())
                .put("address", userProfileEntity.getAddress())
                .put("telf", userProfileEntity.getTelf()).toString();
    }
}
