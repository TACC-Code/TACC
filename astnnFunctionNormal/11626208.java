class BackupThread extends Thread {
    private byte[] generateHash(byte[] data) throws IOException {
        byte[] digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(data);
            digest = md.digest();
        } catch (Exception e) {
            IOException ioe = new IOException("generateHash failed: " + e);
            ioe.initCause(e);
            throw ioe;
        }
        return digest;
    }
}
