class BackupThread extends Thread {
    private static void sendBytes(InputStream fis, OutputStream os) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        while ((bytes = fis.read(buffer)) != -1) os.write(buffer, 0, bytes);
    }
}
