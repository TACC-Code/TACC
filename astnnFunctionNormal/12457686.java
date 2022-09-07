class BackupThread extends Thread {
    private void copyResource(InputStream io, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buf = new byte[256];
        int read = 0;
        while ((read = io.read(buf)) > 0) {
            fos.write(buf, 0, read);
        }
        fos.close();
        io.close();
    }
}
