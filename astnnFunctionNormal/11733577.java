class BackupThread extends Thread {
    public static Document create(Document document, PdfWriter writer, HttpServletRequest req, int fields) {
        try {
            PdfPTable table = new PdfPTable(2);
            PdfPCell cell;
            table.getDefaultCell().setPadding(5f);
            boolean readOnly = false;
            String prefix = "";
            for (int i = 0; i < fields; i++) {
                if (req.getParameter("fr" + i) != null) {
                    readOnly = true;
                    prefix = "<I>";
                } else {
                    readOnly = false;
                    prefix = "";
                }
                table.addCell(req.getParameter("f" + i));
                cell = new PdfPCell();
                cell.setCellEvent(new CreatePDF(writer, prefix + req.getParameter("f" + i), readOnly));
                table.addCell(cell);
            }
            document.add(table);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        }
        return document;
    }
}
