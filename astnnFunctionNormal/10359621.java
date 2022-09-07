class BackupThread extends Thread {
    public static ByteBuffer readFile(MapStore store, File file, int x, int y) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        try {
            int len = new DataInputStream(fin).readInt();
            ByteBuffer buf = store.createMap(x, y, len);
            buf.rewind();
            int read;
            while (len > 0) {
                read = fin.getChannel().read(buf);
                if (read == -1) {
                    throw new IOException("File is too short: " + len + " bytes left");
                }
                Thread.yield();
                len -= read;
            }
            return buf;
        } catch (IOException e) {
            throw e;
        } finally {
            fin.close();
        }
    }
}
