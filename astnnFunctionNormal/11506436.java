class BackupThread extends Thread {
    public static byte[] hash(InputStream stream) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = new byte[1024];
        int i;
        while ((i = stream.read(bytes)) != -1) {
            digest.update(bytes, 0, i);
        }
        stream.close();
        return digest.digest();
    }
}
