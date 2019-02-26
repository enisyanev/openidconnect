package com.example.googleopenid.controllers;

import com.example.googleopenid.bogosecurity.GoogleAuthenticator;
import com.example.googleopenid.models.UserPrincipal;
import com.google.api.client.auth.openidconnect.IdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class AuthenticationController {

    @Autowired
    private GoogleAuthenticator googleAuthenticator;

    @GetMapping("/me")
    public UserPrincipal getMe(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        System.out.println(userPrincipal.getEmail());
        return userPrincipal;
    }

    @PostMapping("/authorize")
    public ResponseEntity<IdToken> authorize(@AuthenticationPrincipal UserPrincipal userDetails,
                                             @RequestParam("code") String code,
                                             @RequestParam("redirect_uri") String redirectUri) {
        IdToken responseBody = this.googleAuthenticator.authorize(userDetails, code, redirectUri);
        if(responseBody != null) {
            return ResponseEntity.ok(responseBody);
        }
        return ResponseEntity.badRequest().build();
    }

}
