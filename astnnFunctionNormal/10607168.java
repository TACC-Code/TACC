class BackupThread extends Thread {
    public static void main(String args[]) throws IOException {
        File temp = File.createTempFile("holy", null);
        RandomAccessFile file = new RandomAccessFile(temp, "rw");
        FileChannel channel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(100);
        putData(0, buffer, channel);
        putData(5000000, buffer, channel);
        putData(50000, buffer, channel);
        System.out.println("Wrote temp file '" + temp.getPath() + "',size = " + channel.size());
        channel.close();
        file.close();
    }
}
