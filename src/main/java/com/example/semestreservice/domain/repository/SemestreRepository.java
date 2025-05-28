package com.example.semestreservice.domain.repository;

import com.example.semestreservice.domain.model.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemestreRepository extends JpaRepository<Semestre, Long> {
}