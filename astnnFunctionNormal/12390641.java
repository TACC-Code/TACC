class BackupThread extends Thread {
    public static void printToStream(InputStream is, OutputStream os) throws IOException {
        byte[] buff = new byte[4096];
        int len = 0;
        while ((len = is.read(buff)) != -1) os.write(buff, 0, len);
        is.close();
    }
}
