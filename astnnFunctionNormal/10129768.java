class BackupThread extends Thread {
    private static FileChannel getChannel(File fileToRead, int numBytesToStrip) throws IOException {
        FileInputStream messageStream = new FileInputStream(fileToRead);
        FileChannel channel = messageStream.getChannel();
        stripBytes(channel, numBytesToStrip);
        return channel;
    }
}
