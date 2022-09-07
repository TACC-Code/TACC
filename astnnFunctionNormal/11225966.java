class BackupThread extends Thread {
    public InputStream getSupportFileStream(String path) throws IOException {
        if (path == null) {
            String message = Logging.getMessage("nullValue.FilePathIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        String ref = this.getSupportFilePath(path);
        if (ref != null) {
            URL url = WWIO.makeURL(path);
            if (url != null) return url.openStream();
        }
        return null;
    }
}
