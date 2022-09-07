class BackupThread extends Thread {
    public static InputStream getResourceAsStream(Class _class, String resource) throws IOException {
        URL url = VSSwingUtil.getResource(_class, resource);
        if (url == null) throw new IOException("Cannot find resource " + resource);
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new IOException(e + "Cannot open resource " + url);
        }
    }
}
