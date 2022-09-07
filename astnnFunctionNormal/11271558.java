class BackupThread extends Thread {
    private InputStream getInputStreamFromURL(String s) throws IOException {
        InputStream result = null;
        try {
            URL url = new URL(s);
            result = url.openStream();
        } catch (MalformedURLException x) {
            debug(" WARN: Failed opening as URL: " + s + ". Will try as File");
            result = new FileInputStream(s);
        }
        return result;
    }
}
