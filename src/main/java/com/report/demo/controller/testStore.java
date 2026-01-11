package com.report.demo.controller;

import com.report.demo.model.ApiClientModel;
import com.report.demo.repository.ApiClientRepository;
import com.report.demo.service.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/teste")
public class testStore {

    private final JwtService jwtService;
    private final ApiClientRepository apiClientRepository;

    public testStore(JwtService jwtService, ApiClientRepository apiClientRepository) {
        this.jwtService = jwtService;
        this.apiClientRepository = apiClientRepository;
    }

    @GetMapping("/store")
    public String teste(@RequestHeader("Authorization") String authHeader){

        String token = authHeader.replace("Bearer ", "");
        String clientId = jwtService.getClientIdFromToken(token);

        if (clientId == null) {
            return "Token inválido ou expirado!";
        }

        Optional<ApiClientModel> clientOpt = apiClientRepository.findByClientId(clientId);

        if (clientOpt.isEmpty()) {
            return "Cliente não encontrado!";
        }

        ApiClientModel client = clientOpt.get();
        String schemaName = client.getName();
        return "Cliente autenticado: " + clientId + " | Schema: " + schemaName;
    }

}
