class BackupThread extends Thread {
    public static InputStream getStream(String uri) throws IOException {
        File file = new File(uri);
        String pathStr = null;
        InputStream stream;
        if (file.exists()) {
            stream = new FileInputStream(file);
            pathStr = file.toString();
        } else {
            URL url = null;
            try {
                url = new URL(uri);
            } catch (MalformedURLException ex) {
                throw new IOException("Invalid path " + uri);
            }
            pathStr = url.toString();
            stream = url.openStream();
        }
        return new BufferedInputStream(stream);
    }
}
