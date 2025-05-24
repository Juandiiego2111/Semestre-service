package com.example.semestreservice.dto;

import java.time.LocalDate;

public record SemestreResponse(
        Long id,
        String nombre,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        boolean activo,
        Long programaId
) {}