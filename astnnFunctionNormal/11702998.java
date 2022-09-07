class BackupThread extends Thread {
    public byte[] getMD5Hash(LocalFile l_file) throws IOException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
        }
        InputStream is = new FileInputStream(l_file.getFile());
        try {
            is = new DigestInputStream(is, md);
        } finally {
            is.close();
        }
        byte[] digest = md.digest();
        return digest;
    }
}
