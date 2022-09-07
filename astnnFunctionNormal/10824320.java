class BackupThread extends Thread {
    public static void copyToStream(InputStream from, OutputStream to) throws Exception {
        byte[] buffer = new byte[8192];
        int bytes_read;
        while (true) {
            bytes_read = from.read(buffer);
            if (bytes_read == -1) break;
            to.write(buffer, 0, bytes_read);
        }
        to.flush();
        from.close();
    }
}
