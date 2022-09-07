class BackupThread extends Thread {
    public static void saveFile(MapStore store, File file, ByteBuffer buf) throws IOException {
        FileOutputStream fout = new FileOutputStream(file);
        buf.rewind();
        try {
            int len = buf.limit();
            new DataOutputStream(fout).writeInt(len);
            while (len > 0) {
                len -= fout.getChannel().write(buf);
                Thread.yield();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            fout.close();
        }
    }
}
