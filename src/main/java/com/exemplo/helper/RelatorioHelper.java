package com.exemplo.helper;

import com.exemplo.model.Linha;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

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

	/**
	 * Insere o nome ao arquivo
	 * @param nome
	 */
	public static void adicionaNome(String nome){
		document.addTitle(nome);
	}

	/**
	 * Adiciona um cabeçalho com imagem à esquerda, texto no meio e texto a direita (Obs.: as larguras são fixas).
	 * @param enderecoImagem endereço da imagem a ser inserida.
	 * @param texto1 texto central.
	 * @param texto2 texto à direita.
	 */
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

	/**
	 * Adiciona um cabeçalho com imagem à esquerda e texto no meio (Obs.: as larguras são fixas).
	 * @param enderecoImagem endereço da imagem a ser inserida.
	 * @param texto1 texto central.
	 */
	public static void adicionaCabecalhoImagem(String enderecoImagem, String texto1) {
		try {
			PdfPTable tabela = new PdfPTable(new float[] {1, 7});
			tabela.setWidthPercentage(100.0f);
			PdfPCell cellImagem = new PdfPCell(Image.getInstance(enderecoImagem));
			PdfPCell cell1 = new PdfPCell(new Paragraph(texto1, FONTE_TIMENEWS));
			cellImagem.setBorder(0);
			cellImagem.setFixedHeight(60.0f);
			cell1.setBorder(0);
			cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			tabela.addCell(cellImagem);
			tabela.addCell(cell1);
			document.add(tabela);
		} catch (Exception de) {
			System.err.println(de.getMessage());
		}
	}

	/**
	 * Adiciona uma imagem no meio, com height fixo (ideal para cabeçalhos no meio da pagina).
	 * @param enderecoImagem endereço da imagem a ser inserida.
	 */
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

	/**
	 * Adiciona um quadro com fonte de título (fonte Helvetica tamanho 12). Obs.: maiúscula ou minúscula são devinidas por quem usar o metodo.
	 * @param texto a ser inserido.
	 * @param fundoEscuro determina se o fundo é cinza ou branco.
	 * @param centralizado determina se o texto fica centralizado.
	 */
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

	/**
	 * Adiciona um quadro com fonte normal (fonte Helvetica tamanho 10). Obs.: maiúscula ou minúscula são devinidas por quem usar o metodo.
	 * @param colunas quantidade de colunas de texto a serem inseridas no quadro.
	 * @param linhas lista de objetos "Linha", cada objeto possui uma lista de Strings (lista celulas) para formar a
	 *                  tablela de acordo com a quantidade de colunas, semelhante a uma planilha de excell:
	 *                  uma tabela tem linhas e cada linha tem uma célula para cada coluna.
	 */
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

	/**
	 * Adiciona um texto com fonte normal (fonte Helvetica tamanho 10). Obs.: maiúscula ou minúscula são devinidas por quem usar o metodo.
	 * @param texto a ser inserido.
	 * @param centralizado determina se o texto fica centralizado.
	 */
	public static void adicionaTexto(String texto, boolean centralizado) {
		try {
			Paragraph paragraph = new Paragraph(texto, FONTE_TEXTOS_NORMAIS);
			if (centralizado) { paragraph.setAlignment(Element.ALIGN_CENTER); }
			document.add(paragraph);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Adiciona um texto com fonte normal com serifas (fonte Time New Roman tamanho 12). Obs.: maiúscula ou minúscula são devinidas por quem usar o metodo.
	 * @param texto a ser inserido.
	 * @param centralizado determina se o texto é centralizado.
	 */
	public static void adicionaTextoTimeNews(String texto, boolean centralizado) {
		try {
			Paragraph paragraph = new Paragraph(texto, FONTE_TIMENEWS);
			if (centralizado) { paragraph.setAlignment(Element.ALIGN_CENTER); }
			document.add(paragraph);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Adiciona um texto com fonte normal com serifas, formatado com espaçamento de parágrafo (fonte Time News Roman tamanho 12).
	 * Obs.: maiúscula ou minúscula são devinidas por quem usar o metodo.
	 * @param texto a ser inserido.
	 */
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

	/**
	 * Adiciona um texto com fonte normal com serifas, com recuo à direita iniciando da metade da pagina
	 * (fonte Time News Roman tamanho 12). Obs.: maiúscula ou minúscula são devinidas por quem usar o metodo.
	 * @param texto texto a ser inserido.
	 */
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

	/**
	 * Adiciona cabeçano e rodapé. (Ainda em fase de conclusõo)
	 * @param arquivo
	 * @param textoCabecalho
	 * @param textoRodape
	 */
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

	/**
	 * Adiciona uma nova página.
	 */
	public static void adicionaNovaPagina() {
		document.newPage();
	}

	/**
	 * Adiciona um texto com fonte de título (fonte Helvetica tamanho 14).
	 * @param texto a ser inserido.
	 * @param centralizado determina se o texto é centralizado.
	 */
	public static void adicionaTextoTitulo(String texto, boolean centralizado) {
		try {
			Paragraph paragraph = new Paragraph(texto, FONTE_TITULO);
			if (centralizado) { paragraph.setAlignment(Element.ALIGN_CENTER); }
			document.add(paragraph);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Adiciona um texto normal alinhado a margem direita da pagina (fonte Helvetica tamanho 10).
	 * @param texto a ser inserido.
	 */
	public static void adicionaTextoADireita(String texto) {
		try {
			Paragraph paragraph = new Paragraph(texto, FONTE_TEXTOS_NORMAIS);
			paragraph.setAlignment(Element.ALIGN_RIGHT);
			document.add(paragraph);
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Adiciona um espaço em branco da altura de uma linha.
	 */
	public static void adicionaLinhaEmBranco() {
		try {
			document.add(new Paragraph(" "));
		} catch (DocumentException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Adiciona um texto em html com fonte time news roman.
	 * @param html string html/xml a ser inserida.
	 */
	public static void adicionaHtmlFonteTimeNews(String html) {
		try {
			Paragraph paragraph = new Paragraph();
			paragraph.setFont(FONTE_TIMENEWS);
			paragraph.addAll(XMLWorkerHelper.parseToElementList(html, "p {font-family: Times;}"));
			paragraph.setFirstLineIndent(70);
			paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(paragraph);
		} catch (DocumentException | IOException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Adiciona uma tabela de dados string.
	 * @param campos lista de titulos das colunas da tabela, o tamanho da lista determina a quantidade de colunas da tabela.
	 * @param linhas lista de objetos "Linha", cada objeto possui uma lista de Strings (lista celulas) para formar a
	 * tablela de acordo com a quantidade de colunas, semelhante a uma planilha de excell:
	 * uma tabela tem linhas e cada linha tem uma célula para cada coluna.
	 */
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

	/**
	 * Fecha e conclui a criação do documento.
	 */
	public static void concluiPdf() {
		document.close();
	}

}
