class BackupThread extends Thread {
    public static void storeStreamInFile(InputStream in, File f) throws IOException {
        f.createNewFile();
        FileOutputStream fos = new FileOutputStream(f);
        byte[] b = new byte[1024];
        int read_size;
        while ((read_size = in.read(b)) != -1) {
            fos.write(b, 0, read_size);
            fos.flush();
        }
        fos.close();
    }
}
