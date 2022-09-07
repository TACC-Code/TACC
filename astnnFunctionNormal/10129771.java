class BackupThread extends Thread {
    private static void writeMessageData(IMessage message, File messageOutputRoot) throws IOException {
        if (messageOutputRoot == null) {
            return;
        }
        int id = message.getMessageId();
        File tempFile = File.createTempFile(String.format("%1$07d_", id), ".dat", messageOutputRoot);
        System.out.println("Writing data to \"" + tempFile.getPath() + "\"");
        FileChannel channel = new FileOutputStream(tempFile).getChannel();
        ByteBuffer buffer = IOUtil.createBuffer(message.getMessageContentSize());
        message.writeMessageContent(buffer);
        buffer.flip();
        channel.write(buffer);
        channel.close();
    }
}
