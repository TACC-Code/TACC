class BackupThread extends Thread {
    private void initSVGSignSource(URL url) throws IOException {
        String ressourceName = url.toString();
        InputStream in = url.openStream();
        initSVGSignSource(ressourceName, in, getCodeForURL(url));
    }
}
