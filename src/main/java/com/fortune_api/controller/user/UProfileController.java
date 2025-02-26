package com.fortune_api.controller.user;

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
    public UserProfileEntity generateProfile(@RequestParam("user_id") final long user_id, @RequestParam("name") final String name, @RequestParam(name = "address") final String address, @RequestParam(name = "phone") final String phone, @RequestParam(name = "online") final boolean online) {
        return userProfileService.generateUserProfile(user_id, name, address, phone, online);
    }

    @GetMapping("/findUserByProfileId")
    public UserProfileEntity findProfileByUserId(@RequestParam("user_id") final long user_id) {
        return userProfileService.findProfileByUserId(user_id);
    }
}
