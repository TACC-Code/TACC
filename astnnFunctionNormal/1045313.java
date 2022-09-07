class BackupThread extends Thread {
    public static String hash(Integer value, String algorithm) {
        String result = "";
        try {
            byte textBytes = value.byteValue();
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(textBytes);
            byte[] codigo = md.digest();
            result = convertToHex(codigo);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("hash ERROR:" + ex.getMessage());
        }
        return result;
    }
}
