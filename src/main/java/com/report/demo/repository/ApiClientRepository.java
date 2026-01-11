package com.report.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.report.demo.model.ApiClientModel;

import java.util.Optional;

public interface ApiClientRepository extends JpaRepository<ApiClientModel, Long> {
    Optional<ApiClientModel> findByClientId(String clientId);
}
