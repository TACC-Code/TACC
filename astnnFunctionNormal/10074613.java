class BackupThread extends Thread {
    public static void main(String[] args) throws IOException, DocumentException {
        new MovieTemplates().createPdf(MovieTemplates.RESULT);
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();
        PdfPTable table = new PdfPTable(2);
        PdfReader reader = new PdfReader(MovieTemplates.RESULT);
        int n = reader.getNumberOfPages();
        PdfImportedPage page;
        for (int i = 1; i <= n; i++) {
            page = writer.getImportedPage(reader, i);
            table.addCell(Image.getInstance(page));
        }
        document.add(table);
        document.close();
    }
}
