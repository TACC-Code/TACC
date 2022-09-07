class BackupThread extends Thread {
    public void add(URL url, String baseURI, RDFFormat dataFormat, Resource... contexts) throws IOException, RDFParseException, RepositoryException {
        if (baseURI == null) {
            baseURI = url.toExternalForm();
        }
        InputStream in = url.openStream();
        try {
            add(in, baseURI, dataFormat, contexts);
        } finally {
            in.close();
        }
    }
}
