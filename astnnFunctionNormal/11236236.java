class BackupThread extends Thread {
    private static String computeChecksum(byte[] toCompute) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(toCompute);
        java.math.BigInteger hash = new java.math.BigInteger(1, md.digest());
        return hash.toString(16);
    }
}
