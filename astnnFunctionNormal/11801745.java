class BackupThread extends Thread {
    public void setup(PipelineContext ctx) throws PipelineException {
        super.setup(ctx);
        if (factory == null) {
            try {
                factory = (SAXTransformerFactory) TransformerFactory.newInstance();
            } catch (Exception ex) {
                throw new PipelineException(ex);
            }
        }
        this.ctx = ctx;
        final SourceResolver sourceResolver = ctx.getSourceResolver();
        if (sourceResolver != null) {
            uriResolver = new URIResolver() {

                public Source resolve(String base, String href) {
                    try {
                        URL url = sourceResolver.resolve(href);
                        if (url != null) {
                            return new StreamSource(url.openStream(), url.toExternalForm());
                        }
                    } catch (Exception ex) {
                        log.warn("Unable to resolve url " + href);
                    }
                    return null;
                }
            };
            if (manager.getSourceResolver() == null) {
                manager.setSourceResolver(sourceResolver);
            }
        }
        if (xslPath != null) {
            handler = getHandler(xslPath);
            javax.xml.transform.Transformer trans = handler.getTransformer();
            if (uriResolver != null) {
                trans.setURIResolver(uriResolver);
            }
            trans.setErrorListener(new TraxErrorListener());
            String output = trans.getOutputProperty("method");
            if (!"xml".equals(output)) {
                throw new IllegalArgumentException("The output property of the local xsl stylesheet must be xml");
            }
            contentHandler = handler;
            lexicalHandler = handler;
        }
    }
}
