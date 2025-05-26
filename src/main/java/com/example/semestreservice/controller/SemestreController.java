package com.example.semestreservice.controller;

import com.example.semestreservice.dto.SemestreRequest;
import com.example.semestreservice.dto.SemestreResponse;
import com.example.semestreservice.model.ProgramaDTO;
import com.example.semestreservice.service.IProgramaClient;
import com.example.semestreservice.service.SemestreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.semestreservice.exception.ResourceNotFoundException;
import feign.FeignException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/semestre-service")
@RequiredArgsConstructor
public class SemestreController {

    private final SemestreService semestreService;
    private final IProgramaClient programaClient;

    @GetMapping("/semestres")
    public ResponseEntity<Map<String, Object>> listarSemestres() {
        List<SemestreResponse> lista = semestreService.listarSemestres();
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Lista de semestres obtenida correctamente");
        resp.put("semestres", lista);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/semestres")
    public ResponseEntity<Map<String, Object>> crearSemestre(
            @Valid @RequestBody SemestreRequest request) {

        if (request.programaId() != null) {
            try {
                programaClient.obtenerProgramaPorId(request.programaId());
            } catch (FeignException e) {
                if (e.status() == HttpStatus.NOT_FOUND.value()) {
                    throw new ResourceNotFoundException("El programa con este ID no existe");
                }
                throw new RuntimeException("Error al validar el programa");
            }
        }

        SemestreResponse creado = semestreService.crearSemestre(request);
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Semestre creado correctamente");
        resp.put("semestre", creado);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @GetMapping("/semestres/{id}")
    public ResponseEntity<Map<String, Object>> obtenerSemestre(@PathVariable Long id) {
        try {
            SemestreResponse dto = semestreService.obtenerSemestre(id);
            Map<String, Object> resp = new HashMap<>();
            resp.put("message", "Semestre obtenido correctamente");
            resp.put("semestre", dto);
            return ResponseEntity.ok(resp);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("El semestre con este ID no existe");
        }
    }

    @PutMapping("/semestres/{id}")
    public ResponseEntity<Map<String, Object>> actualizarSemestre(
            @PathVariable Long id,
            @Valid @RequestBody SemestreRequest request) {

        if (request.programaId() != null) {
            try {
                programaClient.obtenerProgramaPorId(request.programaId());
            } catch (FeignException e) {
                if (e.status() == HttpStatus.NOT_FOUND.value()) {
                    throw new ResourceNotFoundException("El programa con este ID no existe");
                }
                throw new RuntimeException("Error al validar el programa");
            }
        }

        try {
            SemestreResponse updated = semestreService.actualizarSemestre(id, request);
            Map<String, Object> resp = new HashMap<>();
            resp.put("message", "Semestre actualizado correctamente");
            resp.put("semestre", updated);
            return ResponseEntity.ok(resp);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("El semestre con este ID no existe");
        }
    }

    @DeleteMapping("/semestres/{id}")
    public ResponseEntity<Map<String, Object>> eliminarSemestre(@PathVariable Long id) {
        try {
            semestreService.eliminarSemestre(id);
            Map<String, Object> resp = new HashMap<>();
            resp.put("message", "Semestre eliminado correctamente");
            return ResponseEntity.ok(resp);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("El semestre con este ID no existe");
        }
    }
}