class BackupThread extends Thread {
    public final void write(OutputStream out) throws IOException {
        File file = new File(filename);
        long len = file.length();
        FileInputStream in = new FileInputStream(file);
        try {
            FileChannel inch = in.getChannel();
            WritableByteChannel outch = Channels.newChannel(out);
            int pos = 0;
            while (pos < len) {
                pos += inch.transferTo(pos, len - pos, outch);
            }
        } finally {
            in.close();
        }
    }
}
