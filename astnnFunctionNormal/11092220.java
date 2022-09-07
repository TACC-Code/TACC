class BackupThread extends Thread {
    private synchronized byte[] readClassFile(String className) throws IOException {
        String fileName = dotToSlash(className) + ".class";
        InputStream stream = getResourceAsStream(fileName);
        if (stream == null) {
            throw new IOException("Resource not found: " + fileName);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
        for (int c = stream.read(readBuffer); c != -1; c = stream.read(readBuffer)) {
            baos.write(readBuffer, 0, c);
        }
        stream.close();
        baos.close();
        return baos.toByteArray();
    }
}
