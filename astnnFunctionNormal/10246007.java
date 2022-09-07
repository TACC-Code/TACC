class BackupThread extends Thread {
    public static synchronized void writeURLContent(URL url, File destination) throws IOException {
        FileOutputStream fos = new FileOutputStream(destination);
        pump(url.openStream(), fos);
        fos.flush();
        fos.close();
    }
}
