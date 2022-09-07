class BackupThread extends Thread {
    public void buildPresentation(Map<String, String> parameter, OutputStream output) throws XFormDocumentException {
        parameters.putAll(parameter);
        if (dataModelDoc == null) {
            throw new XFormDocumentException("Error, please create a data model first");
        }
        String xsltPath = "mwt/xml/xdbforms/xformlayer/xslt/xdbforms.xsl";
        @SuppressWarnings("static-access") URL url = Thread.currentThread().getContextClassLoader().getResource(xsltPath);
        XFormErrorListener xfel = new XFormErrorListener();
        XFormTransformer xft = new XFormsTransformerImpl(parameters, xfel, output);
        try {
            InputStream stream = url.openStream();
            xft.transform(new StreamSource(stream), new DOMSource(dataModelDoc));
        } catch (XFormTransformerException ex) {
            throw new XFormDocumentException(ex);
        } catch (IOException ioe) {
            throw new XFormDocumentException(ioe);
        }
    }
}
