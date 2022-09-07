class BackupThread extends Thread {
    public static void copy(URL url, File file) throws IOException {
        InputStream inputStream = new BufferedInputStream(url.openStream());
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        try {
            IOUtilities.copy(inputStream, outputStream);
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }
}
