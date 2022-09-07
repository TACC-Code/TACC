class BackupThread extends Thread {
    public static void writeAndClose(Writer writer, Reader reader) {
        try {
            write(writer, reader);
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        } finally {
            safeClose(writer);
            safeClose(reader);
        }
    }
}
