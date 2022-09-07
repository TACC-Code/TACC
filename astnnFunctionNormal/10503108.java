class BackupThread extends Thread {
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[0xFFFF];
        for (int len; (len = in.read(buffer)) != -1; ) out.write(buffer, 0, len);
    }
}
