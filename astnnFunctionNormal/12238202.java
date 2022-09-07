class BackupThread extends Thread {
    @Test
    public void should_writable_channel() throws IOException {
        FileOutputStream os = new FileOutputStream("res/notexist.file");
        FileChannel channel = os.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put(new byte[] { 97, 98, 99 });
        channel.write(buffer);
        channel.close();
        os.close();
        file = new File("res/notexist.file");
    }
}
