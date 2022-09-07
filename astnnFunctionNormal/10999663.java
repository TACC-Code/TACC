class BackupThread extends Thread {
    public static byte[] getBytes(File file) throws FileNotFoundException, IOException {
        ByteBuffer buf = ByteBuffer.allocate((int) file.length());
        FileInputStream fis = new FileInputStream(file);
        fis.getChannel().read(buf);
        fis.close();
        return buf.array();
    }
}
