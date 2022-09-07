class BackupThread extends Thread {
    public static byte[] inputStreamToBytes(InputStream in) throws IOException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
            in.close();
            out.close();
            return out.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException("Error while converting stream to bytes", ex);
        }
    }
}
