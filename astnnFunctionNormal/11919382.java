class BackupThread extends Thread {
    public static String calculateHash(MessageDigest algorithm, InputStream is) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(is);
        DigestInputStream dis = new DigestInputStream(bis, algorithm);
        while (dis.read() != -1) ;
        byte[] hash = algorithm.digest();
        return byteArray2Hex(hash);
    }
}
