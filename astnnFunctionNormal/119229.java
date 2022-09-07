class BackupThread extends Thread {
    void copy(InputStream in, File dest) throws IOException {
        dest.getParentFile().mkdirs();
        OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
        try {
            byte[] data = new byte[8192];
            int n;
            while ((n = in.read(data, 0, data.length)) > 0) out.write(data, 0, n);
        } finally {
            out.close();
            in.close();
        }
    }
}
