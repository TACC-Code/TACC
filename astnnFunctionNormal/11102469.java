class BackupThread extends Thread {
    private byte[] digestFile(String file, String digestAlg) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
        InputStream in = new FileInputStream(file);
        MessageDigest dig = MessageDigest.getInstance(digestAlg, "BC");
        int len = 0;
        byte[] buf = new byte[2048];
        while ((len = in.read(buf)) > 0) {
            dig.update(buf, 0, len);
        }
        byte[] digest = dig.digest();
        in.close();
        return digest;
    }
}
