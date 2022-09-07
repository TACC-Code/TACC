class BackupThread extends Thread {
    public InputStream openInputStream() throws IOException {
        URLConnection connection = _url.openConnection();
        _lastModifiedTime = connection.getLastModified();
        InputStream base = connection.getInputStream();
        if (base instanceof BufferedInputStream) return base; else return new BufferedInputStream(base);
    }
}
