package com.report.demo.config;

import com.report.demo.model.ApiClientModel;
import com.report.demo.repository.ApiClientRepository;
import com.report.demo.service.JwtService;
import com.report.demo.config.TenantContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class TenantFilter implements Filter {

    private final JwtService jwtService;
    private final ApiClientRepository apiClientRepository;

    public TenantFilter(JwtService jwtService, ApiClientRepository apiClientRepository) {
        this.jwtService = jwtService;
        this.apiClientRepository = apiClientRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            String clientId = jwtService.getClientIdFromToken(token);

            if (clientId != null) {
                Optional<ApiClientModel> clientOpt = apiClientRepository.findByClientId(clientId);
                clientOpt.ifPresent(client -> TenantContext.setCurrentTenant(client.getName()));
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

}
