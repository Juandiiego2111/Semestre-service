package com.example.semestreservice.service;

import com.example.semestreservice.model.ProgramaDTO; // Importa desde "model"
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "programa-service")
public interface IProgramaClient {


    @GetMapping("/programas/{id}")
    ProgramaDTO obtenerProgramaPorId(@PathVariable Long id);
}