package com.exemplo.helper;

import com.exemplo.model.Linha;
import com.exemplo.model.Relatorio;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RelatorioHelper {
	
	private static Document document;

	private final static Font FONTE_TOPICOS = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	
	private final static Font FONTE_TEXTOS_NORMAIS = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
	
	private final static Font FONTE_TITULO = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLACK);
	
	public static String geraPdf(Relatorio relatorio){
		String arquivo = null;
		try {
			arquivo = relatorio.getEndereco() + relatorio.getNome().replaceAll("[.\\/\"\'<>\\|\\*\\+%@#$&\\(\\)]", "").trim() + ".pdf";
//			criaPdf(arquivo);
			adicionaNome(relatorio.getNome());
			adicionaCabecalho(relatorio.getCabecalho(), relatorio.getNome());
			adicionaTabelaDados(relatorio.getCampos(), relatorio.getLinhas());
			concluiDocumento();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return arquivo;
	}
    
	public static String criaPdf(String endereco, String nome){
		document = new Document();
		String arquivo = endereco + nome.replaceAll("[.\\/\"\'<>\\|\\*\\+%@#$&\\(\\)]", "").trim() + ".pdf";
		try {
			PdfWriter.getInstance(document, new FileOutputStream(arquivo));
			document.setPageSize(PageSize.A4);
			document.setMargins(20, 20, 20, 20);
			document.open();
		} catch(DocumentException | IOException de) {
            System.err.println(de.getMessage());
        }
		return arquivo;
	}
	
	public static void adicionaNome(String nome){
		document.addTitle(nome);
	}
	
	public static void adicionaCabecalho(String cabecalho, String nome){
		try {
			document.add(new Paragraph(nome, FONTE_TITULO));
			document.add(new Paragraph(cabecalho, FONTE_TEXTOS_NORMAIS));
			document.add(new Paragraph(" "));
		} catch (DocumentException de) {
            System.err.println(de.getMessage());
        }
	}

	public static void adicionaQuadroTitulo(String texto, boolean fundoEscuro, boolean centralizado) {
		try {
			PdfPTable tabela = criaTabela(1, false);
			PdfPCell cell = new PdfPCell(new Paragraph(texto, FONTE_TOPICOS));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			if (fundoEscuro) { cell.setBackgroundColor(BaseColor.LIGHT_GRAY); }
			if (centralizado) { cell.setHorizontalAlignment(Element.ALIGN_CENTER); }
			tabela.addCell(cell);
			document.add(tabela);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void adicionaQuadroTexto(int colunas, List<Linha> linhas) {
		try {
			PdfPTable tabela = criaTabela(colunas, false);
			List<Paragraph> paragraphs = new ArrayList<>();
			for (Linha linha : linhas) {
				for (String celula : linha.getCelulas()) {
					paragraphs.add(new Paragraph(celula, FONTE_TEXTOS_NORMAIS));
				}
			}
			PdfPCell cell = new PdfPCell();
			paragraphs.forEach(cell::addElement);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			tabela.addCell(cell);
			document.add(tabela);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void adicionaTexto(String texto) {
		try {
			document.add(new Paragraph(texto, FONTE_TEXTOS_NORMAIS));
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void adicionaLinhaEmBranco() {
		try {
			document.add(new Paragraph(" "));
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void adicionaTabelaDados(List<String> campos, List<Linha> linhas) {
		int tam = campos.size();
		PdfPTable tabela = criaTabela(tam, true);
		campos.forEach(campo -> {
			PdfPCell celula = new PdfPCell(new Paragraph(campo, FONTE_TOPICOS));
			celula.setBorder(0);
			tabela.addCell(celula);
		});
		int linhaDoc = 2;
		for (Linha linha : linhas) {
			for (String celula : linha.getCelulas()) {
				PdfPCell cell = new PdfPCell(new Paragraph(celula, FONTE_TEXTOS_NORMAIS));
				cell.setBorder(0);
				if(linhaDoc % 2 == 0) { cell.setBackgroundColor(BaseColor.LIGHT_GRAY); }
				tabela.addCell(cell);
			}
			linhaDoc++;
		}
		adicionaTabela(tabela);
	}
	
	private static void adicionaTabela(PdfPTable tabela){
		try {
			document.add(tabela);
			document.add(new Paragraph(" "));
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	private void addCalculo(String tipoTotal, String valor) {
		String somatorio = "* " + tipoTotal + ": " + valor + ";";
		try {
			document.add(new Paragraph(somatorio, FONTE_TOPICOS));
			document.add(new Paragraph(" "));
		} catch (DocumentException de) {
            System.err.println(de.getMessage());
        }
	}
	
	private static PdfPTable criaTabela(int tam, boolean semBorda) {
		PdfPTable tabela = new PdfPTable(tam);
		if (semBorda) { tabela.getDefaultCell().setBorder(PdfPCell.NO_BORDER); }
		tabela.setWidthPercentage(100.0f);
		tabela.setHorizontalAlignment(Element.ALIGN_CENTER);
		return tabela;
	}
	
	public static void concluiDocumento() {
		document.close();
	}
	
}
