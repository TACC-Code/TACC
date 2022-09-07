class BackupThread extends Thread {
    private void createBinaryRequestDocument(NodeRef request) {
        ContentTransformer transformer = contentService.getTransformer(MimetypeMap.MIMETYPE_TEXT_PLAIN, MimetypeMap.MIMETYPE_PDF);
        ProcurementRequest procurementRequest = new ProcurementRequest(request, this.nodeService);
        ContentReader reader = FileContentReader.getSafeContentReader(null, procurementRequest.createTextOutput());
        ContentWriter writer = contentService.getWriter(request, TYPE_CONTENT, true);
        writer.setMimetype(MimetypeMap.MIMETYPE_PDF);
        writer.setEncoding("UTF-8");
        transformer.transform(reader, writer);
    }
}
