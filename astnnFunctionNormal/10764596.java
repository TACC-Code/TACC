class BackupThread extends Thread {
    public final ReadableByteChannel byteChannel() {
        return channel instanceof NBChannel ? ((NBChannel) channel).getChannel() : null;
    }
}
