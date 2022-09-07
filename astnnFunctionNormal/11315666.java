class BackupThread extends Thread {
    public void init() {
        double[] lat = { -10, -10, 10, 10 };
        double[] lon = { 10, -10, -10, 10 };
        ByteBuffer byteBuf = ByteBuffer.allocate(1000);
        byteBuf.order(ByteOrder.LITTLE_ENDIAN);
        try {
            FileChannel pkgChan = new FileOutputStream(new File("Terrain3/pathlist.pkg")).getChannel();
            FileChannel idxChan = new FileOutputStream(new File("Terrain3/pathlist.idx")).getChannel();
            byteBuf.putInt(1);
            byteBuf.put((byte) 4);
            byteBuf.put(new String("Test").getBytes());
            byteBuf.putDouble(-179);
            byteBuf.putDouble(-89);
            byteBuf.putDouble(179);
            byteBuf.putDouble(89);
            byteBuf.putLong(0);
            System.out.println("Pos=" + byteBuf.position() + " limit=" + byteBuf.limit());
            byteBuf.flip();
            System.out.println("Pos=" + byteBuf.position() + " limit=" + byteBuf.limit());
            idxChan.write(byteBuf);
            idxChan.close();
            byteBuf.clear();
            System.out.println("Pos=" + byteBuf.position() + " limit=" + byteBuf.limit());
            byteBuf.putInt(5);
            byteBuf.put((byte) 3);
            for (int i = 0; i <= 4; i++) {
                byteBuf.putDouble(lat[i % 4]);
                byteBuf.putDouble(lon[i % 4]);
                byteBuf.putShort((short) 12);
            }
            System.out.println("Pos=" + byteBuf.position() + " limit=" + byteBuf.limit());
            byteBuf.flip();
            System.out.println("Pos=" + byteBuf.position() + " limit=" + byteBuf.limit());
            pkgChan.write(byteBuf);
            pkgChan.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
