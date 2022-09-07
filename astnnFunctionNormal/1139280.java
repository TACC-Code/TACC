class BackupThread extends Thread {
    public static void download(URL url, File dst) throws IOException {
        InputStream in = url.openStream();
        try {
            saveToFile(in, dst);
        } finally {
            in.close();
        }
    }
}
