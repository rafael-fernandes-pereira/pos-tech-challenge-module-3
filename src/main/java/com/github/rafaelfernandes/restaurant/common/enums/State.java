package com.github.rafaelfernandes.restaurant.common.enums;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;

@Getter
public enum State {
    AC("Acre"),
    AL("Alagoas"),
    AP("Amapá"),
    AM("Amazonas"),
    BA("Bahia"),
    CE("Ceará"),
    DF("Distrito Federal"),
    ES("Espírito Santo"),
    GO("Goiás"),
    MA("Maranhão"),
    MT("Mato Grosso"),
    MS("Mato Grosso do Sul"),
    MG("Minas Gerais"),
    PA("Pará"),
    PB("Paraíba"),
    PR("Paraná"),
    PE("Pernambuco"),
    PI("Piauí"),
    RJ("Rio de Janeiro"),
    RN("Rio Grande do Norte"),
    RS("Rio Grande do Sul"),
    RO("Rondônia"),
    RR("Roraima"),
    SC("Santa Catarina"),
    SP("São Paulo"),
    SE("Sergipe"),
    TO("Tocantins");

    private final String nomeCompleto;

    State(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public static State of(String state){

        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException ecx) {

            HashSet

            throw new ConstraintViolationException(new HashSet<>());
        }

    }
}

