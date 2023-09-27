package com.MSGFoundation.controller;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class KeycloakController {

    @GetMapping({"/login",""})
    public String home(Principal principal){
         KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) principal;
         if(token!= null){
             AccessToken accessToken = token.getAccount().getKeycloakSecurityContext().getToken();
             System.out.println("inicio"+accessToken.getPreferredUsername());
             return "redirect:/home";
         }
         return "redirect:/login";
     }

     @GetMapping("/logout")
    public String logout(HttpServletRequest request){
         return "redirect:" + "http://localhost:9090/auth/realms/msfg-credit-request-identity/protocol/openid-connect/logout";

     }

}
