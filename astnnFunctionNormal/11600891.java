class BackupThread extends Thread {
    public static InputStream getResourceStream(String name) throws IOException {
        URL url = SpriteStore.get().getResourceURL(name);
        if (url == null) {
            return null;
        }
        return url.openStream();
    }
}
