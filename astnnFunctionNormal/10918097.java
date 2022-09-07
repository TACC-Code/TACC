class BackupThread extends Thread {
    @Overrides
    public void toStream(int fromIndex, int toIndex, DataOutput dest) throws IOException {
        if ((dest == this.file) || (toIndex - fromIndex <= 10) || !(dest instanceof RandomAccessFile)) super.toStream(fromIndex, toIndex, dest); else toByteChannel(fromIndex, toIndex, ((RandomAccessFile) dest).getChannel());
    }
}
