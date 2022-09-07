class BackupThread extends Thread {
    public static WebXml getInstance(URL url) {
        try {
            return XMLDocument.read(url.openStream(), WebXml.class);
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }
}
