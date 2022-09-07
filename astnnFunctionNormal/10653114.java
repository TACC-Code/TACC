class BackupThread extends Thread {
    @Test
    public void should_able_to_decode() throws IOException {
        FileInputStream is = new FileInputStream("res/input2.data");
        FileChannel channel = is.getChannel();
        MappedByteBuffer bb = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        CharBuffer buffer = Charset.forName("GB18030").decode(bb);
        assertTrue(buffer.toString().startsWith("《藏地密码》"));
        channel.close();
        is.close();
    }
}
