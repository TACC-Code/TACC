class BackupThread extends Thread {
    public static String getEncryptedPassword(String strPasswordToBeEncrypted) {
        System.out.println(strPasswordToBeEncrypted);
        byte[] defaultBytes = strPasswordToBeEncrypted.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int iCounter = 0; iCounter < messageDigest.length; iCounter++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[iCounter]));
            }
            System.out.println(hexString.toString());
            return hexString.toString();
        } catch (NoSuchAlgorithmException nsae) {
            throw new GenericException(nsae);
        }
    }
}
