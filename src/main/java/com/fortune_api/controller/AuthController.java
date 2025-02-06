package com.fortune_api.controller;

import com.fortune_api.db.entities.UserEntity;
import com.fortune_api.db.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(name = "nif_nie") final String nif_nie, @RequestParam(name = "password") final String password) {
        JSONObject loginStatus = new JSONObject();

        UserEntity user = userService.login(nif_nie, password);

        if (user != null) {
            return loginStatus
                    .put("login_credentials", true)
                    .put("hasDigitalSign", user.getDigital_sign() != null)
                    .toString();
        }

        return loginStatus
                .put("login_credentials", false)
                .put("hasDigitalSign", false)
                .toString();
    }

    @PostMapping("/register")
    public String register(@RequestParam(name = "nif_nie") final String nif_nie, @RequestParam(name = "email") final String email, @RequestParam(name = "password") final String password) {
        JSONObject registerResponse = new JSONObject();
        UserEntity user = userService.register(nif_nie, email, password);

        if (user != null) {
            return registerResponse
                    .put("id", user.getId())
                    .put("nif_nie", user.getNif_nie())
                    .put("email", user.getEmail())
                    .put("password", user.getPassword())
                    .put("digital_sign", user.getDigital_sign()).toString();
        }

        return "null";
    }
}