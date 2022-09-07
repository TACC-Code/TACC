class BackupThread extends Thread {
    public static void writeClob(int chunkSize, Reader reader, Writer charArrayWriter) {
        char[] chunk = new char[chunkSize];
        int len;
        try {
            while ((len = reader.read(chunk, 0, chunkSize)) > -1) {
                charArrayWriter.write(chunk, 0, len);
            }
        } catch (IOException e) {
            throw new OdalRuntimePersistencyException(String.valueOf(e));
        }
    }
}
