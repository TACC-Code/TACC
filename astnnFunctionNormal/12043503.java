class BackupThread extends Thread {
    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[__copyBufferSize];
        int length = 0;
        while ((length = in.read(buffer, 0, __copyBufferSize)) != -1) out.write(buffer, 0, length);
        try {
            out.close();
        } catch (Exception e) {
        }
    }
}
