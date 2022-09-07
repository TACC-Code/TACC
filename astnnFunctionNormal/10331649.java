class BackupThread extends Thread {
    protected void copyFileToStream(File file, OutputStream out) throws IOException {
        FileInputStream in = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int no = 0;
        try {
            while ((no = in.read(buffer)) != -1) out.write(buffer, 0, no);
        } finally {
            in.close();
        }
    }
}
