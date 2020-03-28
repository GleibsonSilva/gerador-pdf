package com.exemplo.helper;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;

public class HeaderFooter extends PdfPageEventHelper {

    private Phrase cabecalho;
    private Phrase rodape;
    private int pagenumber;
    private String caminhoImagem;

    public HeaderFooter(Phrase cabecalho, Phrase rodape, String caminhoImagem) {
        this.cabecalho = cabecalho;
        this.rodape = rodape;
        this.caminhoImagem = caminhoImagem;
    }

    public void onChapter(PdfWriter writer, Document document,
                          float paragraphPosition, Paragraph title) {
        pagenumber = 1;
    }

    public void onStartPage(PdfWriter writer, Document document) {
        pagenumber++;
    }

    public void onEndPage(PdfWriter writer, Document document) {
        if (!caminhoImagem.isEmpty()) {
            try {
                Image image = Image.getInstance(caminhoImagem);
                image.setAlignment(Element.ALIGN_CENTER);
                image.setAbsolutePosition(20, 790);
                image.scalePercent(7.5f, 7.5f);
                writer.getDirectContent().addImage(image, true);
            } catch (IOException | DocumentException e) {
                System.err.println(e.getMessage());
            }
        }
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(""), 30, 800, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, cabecalho, 400, 800, 0);

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, rodape, 110, 30, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(String.format("page %d", pagenumber)), 550, 30, 0);

//        Rectangle rect = writer.getBoxSize("art");
//        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("My static header text"), rect.getRight(), rect.getTop(), 0);
//        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(String.format("page %d", pagenumber)), (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
    }

}