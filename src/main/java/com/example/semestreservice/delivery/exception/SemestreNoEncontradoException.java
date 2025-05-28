package com.example.semestreservice.delivery.exception;

public class SemestreNoEncontradoException extends RuntimeException {
    public SemestreNoEncontradoException(Long id) {
        super("El semestre con id " + id + " no fue encontrado.");
    }
}
