package com.example.semestreservice.service;

import com.example.semestreservice.dto.SemestreRequest;
import com.example.semestreservice.dto.SemestreResponse;
import com.example.semestreservice.model.Semestre;
import com.example.semestreservice.repository.SemestreRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SemestreService {

    private final SemestreRepository semestreRepository;
    private final IProgramaClient programaClient;

    @Transactional
    public SemestreResponse crearSemestre(SemestreRequest request) {
        validarDatosSemestre(request);
        Semestre semestre = mapToEntity(request);
        semestre = semestreRepository.save(semestre);
        return mapToResponse(semestre);
    }

    @Transactional(readOnly = true)
    public List<SemestreResponse> listarSemestres() {
        return semestreRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SemestreResponse obtenerSemestre(Long id) {
        Semestre semestre = semestreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el semestre con id: " + id));
        return mapToResponse(semestre);
    }

    @Transactional
    public SemestreResponse actualizarSemestre(Long id, SemestreRequest request) {
        validarDatosSemestre(request);
        Semestre semestre = semestreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el semestre con id: " + id));

        actualizarEntidad(semestre, request);
        semestre = semestreRepository.save(semestre);
        return mapToResponse(semestre);
    }

    @Transactional
    public void eliminarSemestre(Long id) {
        if (!semestreRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró el semestre con id: " + id);
        }
        semestreRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> listarSemestresPaginados(int page) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Semestre> pageResult = semestreRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("semestres", pageResult.getContent().stream()
                .map(this::mapToResponse)
                .toList());
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());

        return response;
    }

    // ---- Métodos auxiliares ----

    private SemestreResponse mapToResponse(Semestre semestre) {
        return new SemestreResponse(
                semestre.getId(),
                semestre.getNombre(),
                semestre.getFechaInicio(),
                semestre.getFechaFin(),
                semestre.isActivo(),
                semestre.getProgramaId(),
                obtenerNombrePrograma(semestre.getProgramaId())
        );
    }

    private Semestre mapToEntity(SemestreRequest request) {
        Semestre semestre = new Semestre();
        actualizarEntidad(semestre, request);
        return semestre;
    }

    private void actualizarEntidad(Semestre semestre, SemestreRequest request) {
        semestre.setNombre(request.nombre());
        semestre.setFechaInicio(request.fechaInicio());
        semestre.setFechaFin(request.fechaFin());
        semestre.setActivo(request.activo());
        semestre.setProgramaId(request.programaId());
    }

    private void validarDatosSemestre(SemestreRequest request) {
        validarFechas(request);
        validarPrograma(request.programaId());
    }

    private void validarFechas(SemestreRequest request) {
        if (request.fechaFin().isBefore(request.fechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio.");
        }
    }

    private void validarPrograma(Long programaId) {
        try {
            if (!programaClient.existePrograma(programaId)) {
                throw new IllegalArgumentException("No se encontró el programa con id: " + programaId);
            }
        } catch (FeignException e) {
            throw new RuntimeException("No se pudo verificar el programa porque falló la comunicación con el programa-service.");
        }
    }

    private String obtenerNombrePrograma(Long programaId) {
        try {
            return programaClient.obtenerProgramaPorId(programaId).getNombre();
        } catch (FeignException.NotFound e) {
            return "Programa no encontrado";
        } catch (FeignException e) {
            return "Error al obtener nombre del programa";
        }
    }
}
