package com.example.googleopenid.bogosecurity;

import com.example.googleopenid.models.UserPrincipal;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleAuthenticator {
    private JacksonFactory jacksonFactory;
    private HttpTransport httpTransport;

    @Value("${google.client.id}")
    private String cliendId;

    @Value("${google.client.secret}")
    private String clientSecret;

    public GoogleAuthenticator() {
        this.jacksonFactory = new JacksonFactory();
        this.httpTransport = new NetHttpTransport();
    }

    public GoogleIdToken authorize(UserPrincipal userPrincipal, String authorizationCode, String redirectUri) {
        GoogleIdToken googleIdToken = null;
        try {
            GoogleTokenResponse googleTokenResponse = this.getGoogleTokenResponse(authorizationCode,redirectUri);

            if(userPrincipal == null) {
                userPrincipal = new UserPrincipal();
            }

            try {
                googleIdToken = this.getGoogleIdToken(googleTokenResponse);
                userPrincipal.setEmail(googleIdToken.getPayload().getEmail());
                userPrincipal.setOauthId(googleIdToken.getPayload().getSubject());

                Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal,null,Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleIdToken;
    }

    protected GoogleTokenResponse getGoogleTokenResponse(String authorizationCode, String redirectUri) throws IOException {
        GoogleAuthorizationCodeTokenRequest authorizationCodeTokenRequest =
                new GoogleAuthorizationCodeTokenRequest(httpTransport,
                        jacksonFactory,
                        this.cliendId,
                        this.clientSecret,
                        authorizationCode,
                        redirectUri);

        return authorizationCodeTokenRequest.execute();
    }

    protected GoogleIdToken getGoogleIdToken(GoogleTokenResponse googleTokenResponse) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier
                .Builder(httpTransport,jacksonFactory)
                .setAudience(Collections.singletonList(this.cliendId))
                .build();

        return googleIdTokenVerifier.verify(googleTokenResponse.getIdToken());
    }
}
