class BackupThread extends Thread {
    public static ByteArrayOutputStream getByteArrayOutputStream(String fileName) throws IOException {
        InputStream stream = ResourceLoader.toInputStream(fileName);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            while (stream.available() > 0) {
                byteStream.write(stream.read());
            }
            return byteStream;
        } finally {
            stream.close();
        }
    }
}
