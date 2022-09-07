class BackupThread extends Thread {
    public static void copyStream(InputStream is, OutputStream os, int chunk) throws IOException {
        if (chunk < 2) {
            for (int i = -1; (i = is.read()) >= 0; ) os.write(i);
        } else {
            byte[] buf = new byte[chunk];
            int i;
            while ((i = is.read(buf)) > 0) os.write(buf, 0, i);
        }
    }
}
