class BackupThread extends Thread {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Missing argument: message data file");
            return;
        }
        File fileToRead = getFileToRead(args[0]);
        if (fileToRead == null) {
            return;
        }
        int numBytesToStrip = getNumBytesToStrip(args);
        if (numBytesToStrip == -1) {
            return;
        }
        File messageOutputRoot = getMessageOutputRoot(args);
        ReadableByteChannel channel = getChannel(fileToRead, numBytesToStrip);
        ChannelReader reader = new ChannelReader(channel);
        readAllMessages(reader, messageOutputRoot);
    }
}
