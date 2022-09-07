class BackupThread extends Thread {
    protected byte[] readUntilEOF(InputStream input) throws IOException {
        final int SIZE = 2048;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(SIZE);
        byte[] buffer = new byte[SIZE];
        int n = 0;
        while ((n = input.read(buffer, 0, SIZE)) > 0) baos.write(buffer, 0, n);
        return baos.toByteArray();
    }
}
