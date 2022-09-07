class BackupThread extends Thread {
    private void writeUrlContentsToStream(String url, OutputStream outputStream) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        try {
            int i = 0;
            while ((i = inputStream.read()) != -1) {
                bos.write(i);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }
}
