class BackupThread extends Thread {
    private static void toFile(InputStream ins, FileOutputStream fos, int len, int buf_size) throws java.io.FileNotFoundException, java.io.IOException {
        byte[] buffer = new byte[buf_size];
        int len_read = 0;
        int total_len_read = 0;
        while (total_len_read + buf_size <= len) {
            len_read = ins.read(buffer);
            total_len_read += len_read;
            fos.write(buffer, 0, len_read);
            fos.flush();
        }
        if (total_len_read < len) {
            toFile(ins, fos, len - total_len_read, buf_size / 2);
        }
    }
}
