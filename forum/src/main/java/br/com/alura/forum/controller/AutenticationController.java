package br.com.alura.forum.controller;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.controller.dto.TokenDto;
import br.com.alura.forum.controller.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDto> autenticantion(@RequestBody @Valid LoginForm form){
        try {
            UsernamePasswordAuthenticationToken dadosLogin = form.converter();
            Authentication authentication = authenticationManager.authenticate(dadosLogin);

            String token = tokenService.createToken(authentication);
            return ResponseEntity.ok(new TokenDto(token, "Bearer"));

        } catch (AuthenticationException ex){
            return ResponseEntity.badRequest().build();
        }

    }
}
