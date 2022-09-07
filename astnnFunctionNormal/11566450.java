class BackupThread extends Thread {
    public static void saveFile(final URL url, final File file) throws IOException {
        saveFile(url.openStream(), file);
    }
}
