package com.github.rafaelfernandes.common.exception;

public class ReservationDuplicateException extends RuntimeException{

    private static final String ERROR = "Reserva para essa data e para esse horário já existe!";

    public ReservationDuplicateException() {
        super(ERROR);
    }

    @Override
    public String getMessage() {
        return ERROR;
    }
}

