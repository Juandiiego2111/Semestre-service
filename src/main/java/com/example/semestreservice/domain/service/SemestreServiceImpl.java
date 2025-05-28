package com.example.semestreservice.domain.service;

import com.example.semestreservice.domain.model.Semestre;
import com.example.semestreservice.domain.repository.SemestreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SemestreServiceImpl implements ISemestreService{
    SemestreRepository semestreRepository;
    public SemestreServiceImpl(SemestreRepository semestreRepository) {this.semestreRepository = semestreRepository;}

    @Override
    @Transactional
    public Semestre save(Semestre semestre) {return semestreRepository.save(semestre);}

    @Override
    @Transactional
    public void delete(Semestre semestre) {semestreRepository.delete(semestre);}

    @Transactional(readOnly = true)
    @Override
    public Optional<Semestre> findById(long id) {return semestreRepository.findById(id);}

    @Override
    @Transactional
    public Semestre update(Semestre semestre) {return semestreRepository.save(semestre);}

    @Override
    @Transactional(readOnly = true)
    public List<Semestre> findAll() {return semestreRepository.findAll();}

    @Override
    @Transactional(readOnly = true)
    public Page<Semestre> findAll(Pageable pageable) {return semestreRepository.findAll(pageable);}
}