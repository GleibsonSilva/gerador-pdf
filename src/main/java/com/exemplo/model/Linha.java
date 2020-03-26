package com.exemplo.model;

import java.util.List;

public class Linha {

    private List<String> celulas;

    public Linha() {
    }

    public Linha(List<String> celulas) {
        this.celulas = celulas;
    }

    public List<String> getCelulas() {
        return celulas;
    }

    public void setCelulas(List<String> celulas) {
        this.celulas = celulas;
    }
}
