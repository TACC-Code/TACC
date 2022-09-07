class BackupThread extends Thread {
    private static FileModel loadMD2File(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        FileChannel chan = fis.getChannel();
        ByteBuffer buf = chan.map(FileChannel.MapMode.READ_ONLY, 0, fis.available());
        FileModel md2p = loadMD2File(buf);
        chan.close();
        fis.close();
        return md2p;
    }
}
