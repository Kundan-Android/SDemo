package com.caliber.shwaasdemo.Utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by Caliber on 14-02-2018.
 */

public class HeaderFooterPageEvent extends PdfPageEventHelper {

    Font ffont = new Font(Font.FontFamily.UNDEFINED, 10, Font.ITALIC |  Font.UNDERLINE);
    Font FONT = new Font(Font.FontFamily.HELVETICA, 50, Font.BOLD, new GrayColor(0.85f));
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase header = new Phrase("", ffont);
        Phrase footer = new Phrase("Powered by Caltech Innovations Pvt. Ltd, Bangalore", ffont);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                header,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.top() + 10, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
        ColumnText.showTextAligned(writer.getDirectContentUnder(),
                Element.ALIGN_CENTER, new Phrase("Powered by Caltech Innovations Pvt. Ltd", FONT),
                297.5f, 421,  writer.getPageNumber() % 2 == 1 ? 45 : -45);
    }
}
