package com.example.googleopenid.bogosecurity;

import com.example.googleopenid.models.UserPrincipal;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service("googleAuthenticator")
<<<<<<< HEAD
public class GoogleAuthenticator{
    private JacksonFactory jacksonFactory;
    private HttpTransport httpTransport;

    @Value
    ("${spring.security.oauth2.client.registration.google.client.id}")
    private String cliendId;
    @Value
    ("${spring.security.oauth2.client.registration.google.client.secret}")
=======
public class GoogleAuthenticator {
    private JsonFactory jsonFactory;
    private HttpTransport transport;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
>>>>>>> 96c16f47089cd9a238c447f809a5b06fe57aafd4
    private String clientSecret;


    public GoogleAuthenticator() {
        this.jsonFactory = new JacksonFactory();
        this.transport = new NetHttpTransport();
    }

    public GoogleIdToken authorize(UserPrincipal userPrincipal, String authorizationCode, String redirectUri) {
        GoogleIdToken googleIdToken = null;
        try {
            GoogleTokenResponse tokenResponse = this.getGoogleTokenResponse(authorizationCode, redirectUri);

            if (userPrincipal == null) {
                userPrincipal = new UserPrincipal();
            }

            try {
                googleIdToken = this.getGoogleIdToken(tokenResponse);
                userPrincipal.setEmail(googleIdToken.getPayload().getEmail());
                userPrincipal.setOauthId(googleIdToken.getPayload().getSubject());

                Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleIdToken;
    }

    protected GoogleTokenResponse getGoogleTokenResponse(String authorizationCode, String redirectUri) throws IOException {
        GoogleAuthorizationCodeTokenRequest authorizationCodeTokenRequest =
                new GoogleAuthorizationCodeTokenRequest(transport,
                        jsonFactory,
                        this.clientId,
                        this.clientSecret,
                        authorizationCode,
                        redirectUri);

        return authorizationCodeTokenRequest.execute();
    }

    protected GoogleIdToken getGoogleIdToken(GoogleTokenResponse tokenResponse) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier =
                new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                        .setAudience(Collections.singletonList(this.clientId))
                        .build();
        return verifier.verify(tokenResponse.getIdToken());
    }

}
