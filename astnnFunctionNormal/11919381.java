class BackupThread extends Thread {
    public static String calculateHash(MessageDigest algorithm, File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DigestInputStream dis = new DigestInputStream(bis, algorithm);
        while (dis.read() != -1) ;
        byte[] hash = algorithm.digest();
        return byteArray2Hex(hash);
    }
}
