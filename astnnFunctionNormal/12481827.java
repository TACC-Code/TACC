class BackupThread extends Thread {
    public static void writeClob(Reader reader, Writer charArrayWriter) {
        writeClob(TypeHandler.DEFAULT_CHUNK_SIZE, reader, charArrayWriter);
    }
}
