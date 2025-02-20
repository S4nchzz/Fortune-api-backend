package com.fortune_api.controller;

import com.fortune_api.db.entities.UserProfileEntity;
import com.fortune_api.db.services.UProfileService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userprofile")
public class UProfileController {
    @Autowired
    private UProfileService userProfileService;

    @PostMapping("/generateProfile")
    public String generateProfile(@RequestParam("user_id") final long user_id, @RequestParam("name") final String name, @RequestParam(name = "address") final String address, @RequestParam(name = "phone") final String phone, @RequestParam(name = "online") final boolean online) {
        final UserProfileEntity userProfileEntity = userProfileService.generateUserProfile(user_id, name, address, phone, online);

        return new JSONObject()
                .put("user_id", userProfileEntity.getUser().getId())
                .put("online", userProfileEntity.isOnline())
                .put("name", userProfileEntity.getName())
                .put("address", userProfileEntity.getAddress())
                .put("phone", userProfileEntity.getPhone()).toString();
    }

    @GetMapping("/findUserByProfileId")
    public String findProfileByUserId(@RequestParam("user_id") final long user_id) {
        final UserProfileEntity userProfileEntity = userProfileService.findProfileByUserId(user_id);

        return new JSONObject()
                .put("user_id", userProfileEntity.getUser().getId())
                .put("online", userProfileEntity.isOnline())
                .put("name", userProfileEntity.getName())
                .put("address", userProfileEntity.getAddress())
                .put("phone", userProfileEntity.getPhone()).toString();
    }
}
