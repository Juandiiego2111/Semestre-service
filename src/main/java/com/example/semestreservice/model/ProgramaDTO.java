package com.example.semestreservice.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProgramaDTO implements Serializable {
    private Long id;

    private boolean activo;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;

    @Positive(message = "La duración debe ser un valor positivo")
    private Long duracion;

    @NotNull(message = "El ID del coordinador es obligatorio")
    private Long idCoordinador;

    @NotNull(message = "El ID de la facultad es obligatorio")
    private Long idFacultad;

    @NotBlank(message = "El nivel académico no puede estar vacío")
    private String nivelAcademico;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Min(value = 1, message = "El número de créditos debe ser al menos 1")
    @Max(value = 200, message = "El número de créditos no puede exceder 200")
    private Byte numeroCreditos;

    @NotBlank(message = "El perfil de egreso no puede estar vacío")
    @Size(max = 1000, message = "El perfil de egreso no puede exceder los 1000 caracteres")
    private String perfilEgreso;
}