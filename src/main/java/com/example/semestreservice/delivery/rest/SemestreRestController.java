package com.example.semestreservice.delivery.rest;

import com.example.semestreservice.delivery.exception.SemestreNoEncontradoException;
import com.example.semestreservice.domain.model.ProgramaDTO;
import com.example.semestreservice.domain.model.Semestre;
import com.example.semestreservice.domain.service.IProgramaClient;
import com.example.semestreservice.domain.service.SemestreServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.semestreservice.delivery.exception.ResourceNotFoundException;
import feign.FeignException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/v1/semestre-service")
public class SemestreRestController {

    private final SemestreServiceImpl semestreService;
    private final IProgramaClient programaClient;
    private static final String MENSAJE = "mensaje";
    private static final String SEMESTRE = "semestre";
    private static final String SEMESTRES = "semestres";

    @Autowired
    public SemestreRestController(SemestreServiceImpl semestreService, IProgramaClient programaClient) {
        this.semestreService = semestreService;
        this.programaClient = programaClient;
    }
    //Listar los semestres
    @GetMapping("/semestres")
    public ResponseEntity<Map<String, Object>> listarSemestres() {
        List<Semestre> lista = semestreService.findAll();
        if (lista.isEmpty()) {
            throw new ResourceNotFoundException("No hay semestres disponibles");
        }
        Map<String, Object> response = new HashMap<>();
        response.put(SEMESTRES, lista);
        return ResponseEntity.ok(response);
    }
    //  Listar semestres por paginas
    @GetMapping("/semestres/page/{page}")
    public ResponseEntity<Object> index(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        Page<Semestre> semestres = semestreService.findAll(pageable);
        if (semestres.isEmpty()) {
            throw new ResourceNotFoundException("No hay semestres disponibles en la página solicitada");
        }
        return ResponseEntity.ok(semestres);
    }
    //Crear semestres
    @PostMapping("/semestres")
    public ResponseEntity<Map<String, Object>> crearSemestre(@Valid @RequestBody Semestre semestre, BindingResult result) {
        if(result.hasErrors()) {
            throw new ResourceNotFoundException("Error de validación: de datos");
        }
        Map<String, Object> response = new HashMap<>();
        comprobarPrograma(semestre.getIdPrograma());
        Semestre nuevoSemestre = semestreService.save(semestre);
        response.put(MENSAJE, "El semestre ha sido creado correctamente");
        response.put(SEMESTRE, nuevoSemestre);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    //Eliminar semestres
    @DeleteMapping("/semestres")
    public ResponseEntity<Map<String, Object>> delete(@RequestBody Semestre semestre) {
        semestreService.findById(semestre.getId())
                .orElseThrow(() -> new SemestreNoEncontradoException(semestre.getId()));
        semestreService.delete(semestre);
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "El semestre ha sido eliminado correctamente");
        response.put(SEMESTRE, null);
        return ResponseEntity.ok(response);
    }
    //Actualizar semestre
    @PutMapping("/semestres")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Semestre semestre, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResourceNotFoundException("Error de validación: " + result.getFieldError().getDefaultMessage());
        }
        comprobarPrograma(semestre.getIdPrograma());
        semestreService.findById(semestre.getId())
                .orElseThrow(() -> new SemestreNoEncontradoException(semestre.getId()));
        Semestre semestreActualizado = semestreService.update(semestre);
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "El semestre ha sido actualizado correctamente");
        response.put(SEMESTRE, semestreActualizado);
        return ResponseEntity.ok(response);
    }
    //Buscar semestre por id
    @GetMapping("/semestres/{id}")
    public ResponseEntity<Map<String, Object>> obtenerSemestre(@PathVariable Long id) {
        Semestre semestre = semestreService.findById(id)
                .orElseThrow(() -> new SemestreNoEncontradoException(id));
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "El semestre ha sido encontrado correctamente");
        response.put(SEMESTRE, semestre);
        return ResponseEntity.ok(response);
    }
    //Verificar si el programa existe
    public void comprobarPrograma(Long idPrograma) {
        Map<String, List<ProgramaDTO>> response = programaClient.idprogramas();
        List<ProgramaDTO> programas = response.get("programas");
        boolean existe = programas.stream()
                .anyMatch(programa -> programa.getId().equals(idPrograma));
        if (!existe) {
            throw new ResourceNotFoundException("El programa con ID " + idPrograma + " no existe");
        }
    }
}