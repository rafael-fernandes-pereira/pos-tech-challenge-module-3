package com.github.rafaelfernandes.service.exception;

public class ServiceDuplicateException extends RuntimeException{

    private static final String ERROR = "Serviço para essa data e para esse horário já existe!";

    public ServiceDuplicateException() {
        super(ERROR);
    }

    @Override
    public String getMessage() {
        return ERROR;
    }
}

