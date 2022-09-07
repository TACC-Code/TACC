class BackupThread extends Thread {
    public static byte[] mergePdf(List<byte[]> documents) throws IOException, DocumentException {
        int pageOffset = 0;
        ArrayList master = new ArrayList();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = null;
        PdfCopy writer = null;
        boolean first = true;
        for (Iterator<byte[]> it = documents.iterator(); it.hasNext(); ) {
            PdfReader reader = new PdfReader(it.next());
            reader.consolidateNamedDestinations();
            int n = reader.getNumberOfPages();
            List bookmarks = SimpleBookmark.getBookmark(reader);
            if (bookmarks != null) {
                if (pageOffset != 0) SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                master.addAll(bookmarks);
            }
            pageOffset += n;
            if (first) {
                first = false;
                document = new Document(reader.getPageSizeWithRotation(1));
                writer = new PdfCopy(document, output);
                document.open();
            }
            PdfImportedPage page;
            for (int i = 0; i < n; ) {
                ++i;
                page = writer.getImportedPage(reader, i);
                writer.addPage(page);
            }
            PRAcroForm form = reader.getAcroForm();
            if (form != null) writer.copyAcroForm(reader);
        }
        if (master.size() > 0) writer.setOutlines(master);
        if (document != null) document.close();
        return output.toByteArray();
    }
}
