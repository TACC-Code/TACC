class BackupThread extends Thread {
    static String readFile(String filename, String str) {
        FileInputStream fileInputStream;
        FileChannel fileChannel;
        StringBuilder sbl = new StringBuilder(2048);
        long fileSize;
        MappedByteBuffer mBuf;
        try {
            fileInputStream = new FileInputStream(filename);
            fileChannel = fileInputStream.getChannel();
            fileSize = fileChannel.size();
            mBuf = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            CharBuffer cb = decoder.decode(mBuf);
            while (cb.hasRemaining()) {
                sbl.append(String.valueOf((char) cb.get()));
            }
            fileChannel.close();
            fileInputStream.close();
        } catch (IOException exc) {
            System.out.println(exc);
            System.exit(1);
        }
        return sbl.toString();
    }
}
