class BackupThread extends Thread {
    public static InputStream getResourceStream(String name) throws IOException {
        URL url = SpriteStore.get().getResourceURL(name);
        if (url == null) {
            throw new FileNotFoundException(name);
        }
        return url.openStream();
    }
}
