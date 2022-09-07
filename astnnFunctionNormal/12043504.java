class BackupThread extends Thread {
    public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int length = 0;
        while ((length = in.read(buffer, 0, bufferSize)) != -1) out.write(buffer, 0, length);
        try {
            out.close();
        } catch (Exception e) {
        }
    }
}
