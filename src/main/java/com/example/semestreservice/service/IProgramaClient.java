package com.example.semestreservice.service;

import com.example.semestreservice.model.ProgramaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "programa-service")
public interface IProgramaClient {

    @GetMapping("/api/v1/programa-service/programas/{id}")
    ResponseEntity<Map<String, Object>> obtenerProgramaPorId(@PathVariable Long id);

}