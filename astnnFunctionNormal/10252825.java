class BackupThread extends Thread {
    private ByteBuffer load(File in) throws Exception {
        FileInputStream fis = new FileInputStream(in);
        ByteBuffer data = ByteBuffer.allocate((int) in.length());
        data.order(ByteOrder.LITTLE_ENDIAN);
        FileChannel fischan = fis.getChannel();
        fischan.read(data);
        data.position(0);
        fis.close();
        return data;
    }
}
