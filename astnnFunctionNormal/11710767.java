class BackupThread extends Thread {
    private InputStream getInputStream() throws IOException {
        if (currentSourceType == SourceType.URL) {
            return url.openStream();
        } else {
            return new FileInputStream(file);
        }
    }
}
