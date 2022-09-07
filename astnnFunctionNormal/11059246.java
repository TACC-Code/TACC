class BackupThread extends Thread {
    public static ByteArrayOutputStream render(Writer writer) throws GeneralException {
        FopFactory fopFactory = ApacheFopFactory.instance();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransformerFactory transFactory = TransformerFactory.newInstance();
        try {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            Transformer transformer = transFactory.newTransformer();
            Reader reader = new StringReader(writer.toString());
            Source src = new StreamSource(reader);
            Result res = new SAXResult(fop.getDefaultHandler());
            try {
                transformer.transform(src, res);
                fopFactory.getImageFactory().clearCaches();
                return out;
            } catch (TransformerException e) {
                Debug.logError("FOP transform failed:" + e, module);
                throw new GeneralException("Unable to transform FO to PDF", e);
            }
        } catch (TransformerConfigurationException e) {
            Debug.logError("FOP TransformerConfiguration Exception " + e, module);
            throw new GeneralException("Transformer Configuration Error", e);
        } catch (FOPException e) {
            Debug.logError("FOP Exception " + e, module);
            throw new GeneralException("FOP Error", e);
        }
    }
}
