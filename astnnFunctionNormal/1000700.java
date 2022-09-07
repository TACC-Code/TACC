class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        File file = new File("c:/mem.txt");
        int length = (int) file.length();
        String mode = "rw";
        FileChannel fc = new RandomAccessFile(file, mode).getChannel();
        MappedByteBuffer mbb = fc.map(MapMode.READ_WRITE, 0, length);
        String format = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        byte[] b = new byte[format.length()];
        while (true) {
            mbb.rewind();
            mbb.get(b, 0, b.length);
            String now = new String(b);
            System.out.println(now);
            Thread.sleep(1000);
        }
    }
}
