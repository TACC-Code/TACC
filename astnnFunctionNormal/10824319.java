class BackupThread extends Thread {
    public static void copyToFile(InputStream from, boolean close, File to) throws Exception {
        FileOutputStream out = new FileOutputStream(to);
        byte[] buffer = new byte[8192];
        int bytes_read;
        while (true) {
            bytes_read = from.read(buffer);
            if (bytes_read == -1) break;
            out.write(buffer, 0, bytes_read);
        }
        out.flush();
        out.close();
        if (close) from.close();
    }
}
