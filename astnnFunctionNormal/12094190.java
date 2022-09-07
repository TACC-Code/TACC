class BackupThread extends Thread {
    private static final byte[] hash(InputStream is, String method) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance(method);
            int num;
            byte[] buffer = new byte[1024];
            while ((num = is.read(buffer)) > 0) {
                md.update(buffer, 0, num);
            }
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            log.fatal("Hashing failed", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return null;
    }
}
