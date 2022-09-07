class BackupThread extends Thread {
    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        int count;
        byte[] buffer = new byte[1024];
        while ((count = is.read(buffer)) > 0) os.write(buffer, 0, count);
    }
}
