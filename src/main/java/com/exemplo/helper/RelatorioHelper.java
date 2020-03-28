package com.exemplo.helper;

import com.exemplo.model.Linha;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RelatorioHelper {

	private static Document document;
	private static int[] margens;
	private final static Font FONTE_TOPICOS = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
	private final static Font FONTE_TEXTOS_NORMAIS = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
	private final static Font FONTE_TITULO = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
	private final static Font FONTE_TIMENEWS = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);

	/**
	 * Cria o arquivo no formato PDF.
	 * @param endereco para salvar o arquivo.
	 * @param nome do arquivo.
	 * @param margensDoc array de int de 4 posições determinando as margens, nessa ordem: esquerda, direita, cima, baixo.
	 * @return caminho completo do arquivo gerado.
	 */
	public static String criaPdf(String endereco, String nome, int[] margensDoc){
		document = new Document();
		margens = margensDoc;
		String arquivo = endereco + nome.replaceAll("[.\\/\"\'<>\\|\\*\\+%@#$&\\(\\)]", "").trim() + ".pdf";
		try {
			PdfWriter.getInstance(document, new FileOutputStream(arquivo));
			document.setPageSize(PageSize.A4);
			document.setMargins(margens[0], margens[1], margens[2], margens[3]);
			document.open();
		} catch(DocumentException | IOException de) {
            System.err.println(de.getMessage());
        }
		return arquivo;
	}

	public static void adicionaNome(String nome){
		document.addTitle(nome);
	}

	public static void adicionaCabecalhoImagem(String enderecoImagem, String texto1, String texto2){
		try {
			PdfPTable tabela = new PdfPTable(new float[] {1, 3.5f, 3.5f});
			tabela.setWidthPercentage(100.0f);
			PdfPCell cellImagem = new PdfPCell(Image.getInstance(enderecoImagem));
			PdfPCell cell1 = new PdfPCell(new Paragraph(texto1, FONTE_TOPICOS));
			PdfPCell cell2 = new PdfPCell(new Paragraph(texto2, FONTE_TOPICOS));
			cellImagem.setBorder(0);
			cellImagem.setFixedHeight(60.0f);
			cell1.setBorder(0);
			cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell2.setBorder(0);
			cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tabela.addCell(cellImagem);
			tabela.addCell(cell1);
			tabela.addCell(cell2);
			document.add(tabela);
		} catch (Exception de) {
            System.err.println(de.getMessage());
		}
	}

	public static void adicionaImagemCentro(String enderecoImagem) {
		try {
			PdfPTable tabela = criaTabela(1, true);
			tabela.setWidthPercentage(100.0f);
			PdfPCell cellImagem = new PdfPCell(Image.getInstance(enderecoImagem));
			cellImagem.setBorder(0);
			cellImagem.setFixedHeight(60.0f);
			cellImagem.setHorizontalAlignment(Element.ALIGN_CENTER);
			tabela.addCell(cellImagem);
			document.add(tabela);
		} catch (Exception de) {
			System.err.println(de.getMessage());
		}
	}

	public static void adicionaQuadroTitulo(String texto, boolean fundoEscuro, boolean centralizado) {
		try {
			PdfPTable tabela = criaTabela(1, false);
			PdfPCell cell = new PdfPCell(new Paragraph(texto, FONTE_TOPICOS));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
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
			for (Linha linha : linhas) {
				for (String celula : linha.getCelulas()) {
					PdfPCell cell = new PdfPCell(new Paragraph(celula, FONTE_TEXTOS_NORMAIS));
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorder(0);
					tabela.addCell(cell);
				}
			}
			document.add(tabela);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void adicionaTexto(String texto, boolean centralizado) {
		try {
			Paragraph paragraph = new Paragraph(texto, FONTE_TEXTOS_NORMAIS);
			if (centralizado) { paragraph.setAlignment(Element.ALIGN_CENTER); }
			document.add(paragraph);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void adicionaTextoTimeNews(String texto, boolean centralizado) {
		try {
			Paragraph paragraph = new Paragraph(texto, FONTE_TIMENEWS);
			if (centralizado) { paragraph.setAlignment(Element.ALIGN_CENTER); }
			document.add(paragraph);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void adicionaParagrafoTimeNews(String texto) {
		try {
			Paragraph paragraph = new Paragraph(texto, FONTE_TIMENEWS);
			paragraph.setFirstLineIndent(70);
			paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(paragraph);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void adicionaTextoRecuoDireitaTimeNews(String texto) {
		try {
			Paragraph paragraph = new Paragraph(texto, FONTE_TIMENEWS);
			paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
			paragraph.setIndentationLeft(210);
			document.add(paragraph);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void adicionaCabecalhoRodape(String arquivo, String textoCabecalho, String textoRodape) {
		try {
			Paragraph paragraphC = new Paragraph(textoCabecalho, FONTE_TIMENEWS);
			paragraphC.setAlignment(Element.ALIGN_CENTER);
			Phrase cabecalho = new Phrase(paragraphC);

			Paragraph paragraphR = new Paragraph(textoRodape, FONTE_TIMENEWS);
			paragraphR.setAlignment(Element.ALIGN_CENTER);
			Phrase rodape = new Phrase(paragraphR);

			Document doc = new Document();
			doc.setPageSize(PageSize.A4);
			doc.setMargins(margens[0], margens[1], margens[2], margens[3]);
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(arquivo));
			HeaderFooter event = new HeaderFooter(cabecalho, rodape, "");
			writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
			writer.setPageEvent(event);
		} catch (DocumentException | IOException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void adicionaNovaPagina() {
		document.newPage();
	}

	public static void adicionaTextoTitulo(String texto, boolean centralizado) {
		try {
			Paragraph paragraph = new Paragraph(texto, FONTE_TITULO);
			if (centralizado) { paragraph.setAlignment(Element.ALIGN_CENTER); }
			document.add(paragraph);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void adicionaTextoADireita(String texto) {
		try {
			Paragraph paragraph = new Paragraph(texto, FONTE_TEXTOS_NORMAIS);
			paragraph.setAlignment(Element.ALIGN_RIGHT);
			document.add(paragraph);
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
		try {
			int tam = campos.size();
			PdfPTable tabela = criaTabela(tam, true);
			campos.forEach(campo -> {
				PdfPCell cell = new PdfPCell(new Paragraph(campo, FONTE_TOPICOS));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBorder(0);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tabela.addCell(cell);
			});
			int linhaDoc = 2;
			for (Linha linha : linhas) {
				for (String celula : linha.getCelulas()) {
					PdfPCell cell = new PdfPCell(new Paragraph(celula, FONTE_TEXTOS_NORMAIS));
					cell.setBorder(0);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					if (linhaDoc % 2 != 0) {
						cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					}
					tabela.addCell(cell);
				}
				linhaDoc++;
			}
			document.add(tabela);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	private static PdfPTable criaTabela(int tam, boolean semBorda) {
		PdfPTable tabela = new PdfPTable(tam);
		if (semBorda) { tabela.getDefaultCell().setBorder(PdfPCell.NO_BORDER); }
		tabela.setWidthPercentage(100.0f);
		tabela.setHorizontalAlignment(Element.ALIGN_CENTER);
		return tabela;
	}

	public static void concluiPdf() {
		document.close();
	}

}
