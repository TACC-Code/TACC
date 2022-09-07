class BackupThread extends Thread {
    public void testNIOChannelsReader() throws Exception {
        File f = new File(FNAME);
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        int sz = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
        try {
            Iterable<String> lines = unmemoize(Text.readLines, new BufferedReader(Channels.newReader(new ByteBufferToReadableByteChannel(bb), "us-ascii"), 1000));
            doit.apply(lines);
        } finally {
            fc.close();
        }
    }
}
