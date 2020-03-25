package com.exemplo;

import com.exemplo.helper.RelatorioHelper;
import com.exemplo.model.Linha;
import com.exemplo.model.Relatorio;

import java.util.Arrays;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        geraRequerimento();
    }

    public static void geraRequerimento() {
        Relatorio relatorio = new Relatorio();
        relatorio.setNome("Requerimento/2019");
        relatorio.setCabecalho("ESTADO");
        relatorio.setCampos(Collections.singletonList("DADOS DO REQUERENTE"));
        Linha linha1 = new Linha();
        linha1.setCelulas(Arrays.asList("Razão Social: TRTRQ RPRSNTC CMRC SRVCS LTD"));
        Linha linha2 = new Linha();
        linha2.setCelulas(Collections.singletonList("Inscrição: 1041974"));
        relatorio.setLinhas(Arrays.asList(linha1, linha2));
        relatorio.setEndereco("C:/Users/gleibson.borges/Downloads/");

        String arquivo = RelatorioHelper.criaPdf(relatorio.getEndereco(), relatorio.getNome());
        RelatorioHelper.adicionaNome(relatorio.getNome());
        RelatorioHelper.adicionaQuadroTitulo("DADOS DO REQUERENTE", true, true);
        RelatorioHelper.adicionaQuadroTexto(2, relatorio.getLinhas());
        RelatorioHelper.concluiDocumento();

        System.out.println(arquivo);
    }
}
