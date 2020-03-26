package com.exemplo;

import com.exemplo.helper.RelatorioHelper;
import com.exemplo.model.Linha;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        geraRequerimento();
    }

    public static void geraRequerimento() {
        List<Linha> dadosContribuinte = new ArrayList<>();
        Linha linha1 = new Linha(Arrays.asList("Razão Social: TRTRQ RPRSNTC CMRC SRVCS LTD", ""));
        Linha linha2 = new Linha(Arrays.asList("Inscrição: 1041974", "CNPJ: 08.699.976/0001-06"));
        Linha linha3 = new Linha(Arrays.asList("Tipo de Contribuinte: COMERCIANTE VAREJISTA", ""));
        Linha linha4 = new Linha(Arrays.asList("Logradouro: AVENIDA J LS SPRT SNT", ""));
        Linha linha5 = new Linha(Arrays.asList("Número: LT 4/5", "Bairro: JRDM CLFRN"));
        Linha linha6 = new Linha(Arrays.asList("Complemento: QDR K", ""));
        Linha linha7 = new Linha(Arrays.asList("Cidade: FORMOSA", "UF: GO"));
        Collections.addAll(dadosContribuinte, linha1, linha2, linha3, linha4, linha5, linha6, linha7);

        List<Linha> regimesEspeciais = new ArrayList<>();
        Linha regime1 = new Linha(Arrays.asList("2002", "DIESEL TRANSP. COLETIVO", "isenção"));
        Linha regime2 = new Linha(Arrays.asList("3003", "TRANSM. ENERGIA BAGAÇO CANA", "REDUÇÃO BASE DE CÁLCULO"));
        Collections.addAll(regimesEspeciais, regime1, regime2);

        String arquivo = RelatorioHelper.criaPdf("C:/Users/gleibson.borges/Downloads/",
                "Requerimento/2019", new int[]{20, 20, 20, 20});
        RelatorioHelper.adicionaNome("Requerimento/2019");
        RelatorioHelper.adicionaCabecalhoImagem("src/main/resources/img/logo_economia.PNG",
                "ESTADO DE GOIÁS\n\nSECRETARIA DA ECONOMIA",
                "REQUERIMENTO DE REGIME ESPECIAL\n\nSOLICITAÇÃO Nº 1/2019");
        RelatorioHelper.adicionaLinhaEmBranco();
        RelatorioHelper.adicionaLinhaEmBranco();
        RelatorioHelper.adicionaQuadroTitulo("DADOS DO CONTRIBUINTE REQUERENTE", true, true);
        RelatorioHelper.adicionaQuadroTexto(2, dadosContribuinte);
        RelatorioHelper.adicionaLinhaEmBranco();
        RelatorioHelper.adicionaQuadroTitulo("REGIMES ESPECIAIS SOLICITADOS", true, true);
        RelatorioHelper.adicionaTabelaDados(Arrays.asList("Código", "Descrição", "Tipo"), regimesEspeciais);
        RelatorioHelper.adicionaLinhaEmBranco();
        RelatorioHelper.adicionaLinhaEmBranco();
        RelatorioHelper.adicionaLinhaEmBranco();
        RelatorioHelper.adicionaLinhaEmBranco();
        RelatorioHelper.adicionaLinhaEmBranco();
        RelatorioHelper.adicionaQuadroTitulo("OBSERVAÇÕES", true, true);
        RelatorioHelper.adicionaQuadroTexto(1,
                Collections.singletonList(new Linha(Collections.singletonList("Solicito um novo benefício para minha empresa baseado em..."))));
        RelatorioHelper.adicionaLinhaEmBranco();
        RelatorioHelper.adicionaQuadroTitulo("IDENTIFICAÇÃO DO RESPONSÁVEL LEGAL DO REQUERENTE", true, true);
        RelatorioHelper.adicionaQuadroTexto(1, Arrays.asList(
                new Linha(Collections.singletonList("Solicitado por: ANDRE LUIZ FREITAS DOS SANTOS")),
                new Linha(Collections.singletonList("CPF/CNPJ: 565.827.901-10")),
                new Linha(Collections.singletonList("Cargo: CONTADOR"))
        ));
        RelatorioHelper.adicionaLinhaEmBranco();
        RelatorioHelper.adicionaTexto("Goiânia, 11 de Fevereiro de 2020", true);
        RelatorioHelper.adicionaLinhaEmBranco();
        RelatorioHelper.adicionaTextoADireita("*Documento deve ser assinado digitalmente.");
        RelatorioHelper.concluiPdf();

        System.out.println(arquivo);
    }
}
