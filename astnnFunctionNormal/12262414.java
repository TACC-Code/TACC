class BackupThread extends Thread {
    public InputStream getByteStream(int i) {
        try {
            URL url = new URL(getSystemId(i));
            return new BufferedInputStream(url.openStream());
        } catch (MalformedURLException x) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }
}
