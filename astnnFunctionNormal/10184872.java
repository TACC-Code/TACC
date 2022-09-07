class BackupThread extends Thread {
    public void onOpenDocument(PdfWriter writer, Document document) {
        PdfReader reader;
        AcroFields form;
        try {
            reader = new PdfReader("resources/odd.pdf");
            odd = writer.getImportedPage(reader, 1);
            form = reader.getAcroFields();
            odd_title = form.getFieldPositions("title").get(0);
            odd_body = form.getFieldPositions("body").get(0);
            reader = new PdfReader("resources/even.pdf");
            even = writer.getImportedPage(reader, 1);
            form = reader.getAcroFields();
            even_title = form.getFieldPositions("title").get(0);
            even_body = form.getFieldPositions("body").get(0);
            bf = BaseFont.createFont();
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
