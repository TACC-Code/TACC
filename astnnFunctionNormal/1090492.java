class BackupThread extends Thread {
    public static byte[] getCertInfoHash(CertificateInfo certInfo) {
        DefensiveTools.checkNull(certInfo, "certInfo");
        ByteBlock data = ByteBlock.createByteBlock(certInfo);
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException impossible) {
            return null;
        }
        byte[] hash = digest.digest(data.toByteArray());
        return hash;
    }
}
