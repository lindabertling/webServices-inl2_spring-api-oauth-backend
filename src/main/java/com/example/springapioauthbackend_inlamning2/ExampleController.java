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
//@CrossOrigin
public class ExampleController {

    private final OAuth2AuthorizedClientService oauthService; // Hämtar access tokens

    @Autowired
    public ExampleController(OAuth2AuthorizedClientService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/user/repos")
    public Mono<String> getRepos(
            @AuthenticationPrincipal OAuth2User principal, //användaren vi är inne på
            Authentication auth) { //autentiseringen / inloggningen
        var oauthToken = (OAuth2AuthenticationToken) auth; // castar till rätt typ av token
        var client = oauthService.loadAuthorizedClient( //laddar in vår token för att få tillbaka en OAuth2AuthorizedClient
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());
        System.out.println(client.getAccessToken().getTokenValue()); // hämtar access token med get token value
//        return principal.getAttributes(); // returnerar all info som finns på användaren

        WebClient webClient = WebClient.create("https://api.github.com/");

        return webClient.get()
                .uri("/user/repos")
                .header("Authorization", "Bearer " + client.getAccessToken().getTokenValue())
                .retrieve()
                .bodyToMono(String.class);
    }


}
