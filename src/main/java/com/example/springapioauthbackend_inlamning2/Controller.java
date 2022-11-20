package com.example.springapioauthbackend_inlamning2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class Controller {

    private final OAuth2AuthorizedClientService oauthService;

    @Autowired
    public Controller(OAuth2AuthorizedClientService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/user/repos")
    public Mono<String> getRepos(
            @AuthenticationPrincipal OAuth2User principal,
            Authentication auth) {
        var oauthToken = (OAuth2AuthenticationToken) auth;
        var client = oauthService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());
        var accessToken = client.getAccessToken().getTokenValue();

        WebClient webClient = WebClient.create("https://api.github.com/");

        return webClient.get()
                .uri("/user/repos")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class);
    }


}
