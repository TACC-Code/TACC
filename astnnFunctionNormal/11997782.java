class BackupThread extends Thread {
    protected InputSource getInputSourceForClassResource(String resource) throws IOException {
        String after = getSystemIdForClassResource(resource);
        URL url = getClass().getResource(resource);
        InputStream is = url.openStream();
        InputSource source = new InputSource(is);
        source.setSystemId(after);
        return source;
    }
}
