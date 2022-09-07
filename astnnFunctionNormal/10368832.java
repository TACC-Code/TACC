class BackupThread extends Thread {
    public static byte[] readFileRaw(File file) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileInputStream is = new FileInputStream(file);
        byte[] buf = new byte[1024];
        int ret;
        while ((ret = is.read(buf)) != -1) os.write(buf, 0, ret);
        return os.toByteArray();
    }
}
