package com.example.semestreservice.domain.service;

import com.example.semestreservice.domain.model.Semestre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ISemestreService {
    Semestre save(Semestre semestre);
    void delete(Semestre semestre);
    Optional<Semestre> findById(long id);
    Semestre update(Semestre semestre);
    List<Semestre> findAll();
    Page<Semestre> findAll(Pageable pageable);
}
