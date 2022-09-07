class BackupThread extends Thread {
    protected void postRepeatChildItems() throws DocumentHandlerException {
        if (segmentDocument == null) {
            return;
        }
        segmentDocument.close();
        try {
            byte[] content = segmentStream.toByteArray();
            PdfReader reader = new PdfReader(content);
            int numPages = reader.getNumberOfPages();
            PdfWriter writer = (PdfWriter) originalDocumentWriter;
            for (int page = 1; page <= numPages; page++) {
                PdfImportedPage importedPage = writer.getImportedPage(reader, page);
                PdfContentByte directContent = writer.getDirectContent();
                directContent.addTemplate(importedPage, 0, 0);
                if ((page != numPages) || hasNext()) {
                    originalDocument.setPageSize(importedPage.getBoundingBox());
                    originalDocument.newPage();
                }
                ++pageCounter;
            }
            documentHandler.setTemplateContext(originalTemplateContext);
            documentHandler.setDocumentAndWriter(originalDocument, originalDocumentWriter, originalResourceRegistry, originalPageEventHandler);
        } catch (IOException ex) {
            throw new DocumentHandlerException(locator(), "Unable to read segment document: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new DocumentHandlerException(locator(), "Document processing failed: " + ex.getMessage(), ex);
        }
    }
}
