package com.report.demo.controller;

import com.report.demo.dto.AuthRequestDTO;
import com.report.demo.repository.ApiClientRepository;
import com.report.demo.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ApiClientRepository clientRepository;
    private final JwtService jwtService;

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> generateToken(@RequestBody AuthRequestDTO request) {

        Map<String, Object> resposta = new HashMap<>();

        return clientRepository.findByClientId(request.getClient_id())
                .filter(c -> c.getClientSecret().equals(request.getClient_secret()))
                .map(c -> {
                    String token = jwtService.generateToken(c.getClientId());
                    resposta.put("access_token", token);
                    resposta.put("token_type", "bearer");
                    resposta.put("expires_in", 3600);
                    return ResponseEntity.status(HttpStatus.OK).body(resposta);
                })
                .orElseGet(() -> {
                    resposta.put("error", "Invalid credentials");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
                });

    }

}
