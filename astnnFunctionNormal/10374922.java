class BackupThread extends Thread {
    public void render(String name, String page, String info, String contentType, String encoding, HttpServletRequest request, HttpServletResponse response) throws ViewHandlerException {
        if (UtilValidate.isEmpty(contentType)) {
            contentType = "application/pdf";
        }
        Writer writer = new StringWriter();
        FopFactory fopFactory = ApacheFopFactory.instance();
        try {
            ScreenRenderer screens = new ScreenRenderer(writer, null, htmlScreenRenderer);
            screens.populateContextForRequest(request, response, servletContext);
            screens.getContext().put("formStringRenderer", new FoFormRenderer(request, response));
            screens.render(page);
        } catch (Throwable t) {
            throw new ViewHandlerException("Problems with the response writer/output stream", t);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransformerFactory transFactory = TransformerFactory.newInstance();
        try {
            Fop fop = fopFactory.newFop(contentType, out);
            Transformer transformer = transFactory.newTransformer();
            Reader reader = new StringReader(writer.toString());
            Source src = new StreamSource(reader);
            Result res = new SAXResult(fop.getDefaultHandler());
            try {
                transformer.transform(src, res);
                fopFactory.getImageFactory().clearCaches();
                response.setContentType(contentType);
                response.setContentLength(out.size());
                try {
                    out.writeTo(response.getOutputStream());
                    response.getOutputStream().flush();
                } catch (IOException e) {
                    throw new ViewHandlerException("Unable write to browser OutputStream", e);
                }
            } catch (TransformerException e) {
                Debug.logError("FOP transform failed:" + e, module);
                throw new ViewHandlerException("Unable to transform FO to " + contentType, e);
            }
        } catch (TransformerConfigurationException e) {
            Debug.logError("FOP TransformerConfiguration Exception " + e, module);
            throw new ViewHandlerException("Transformer Configuration Error", e);
        } catch (FOPException e) {
            Debug.logError("FOP Exception " + e, module);
            throw new ViewHandlerException("FOP Error", e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Debug.logError("Unable to close output stream " + e, module);
            }
        }
    }
}
