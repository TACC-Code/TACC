class BackupThread extends Thread {
    private static byte[] calculateChecksum(File f) {
        DigestInputStream in = null;
        byte[] digest;
        try {
            if (!f.isFile() || !f.canRead()) return null;
            MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
            in = new DigestInputStream(new FileInputStream(f), md);
            byte[] buffer = new byte[2048];
            int read;
            do {
                read = in.read(buffer);
            } while (read > 0);
            digest = md.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close(in);
        }
        return digest;
    }
}
