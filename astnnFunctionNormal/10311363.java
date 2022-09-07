class BackupThread extends Thread {
    private static String calculateMac(String[] values, String secret) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (java.security.NoSuchAlgorithmException nsae) {
            System.out.println("MD5 not found!\n");
        }
        int asciiValue = 0;
        int size = values.length;
        String paramString = "";
        for (int i = 0; i < size; i++) {
            paramString += values[i];
        }
        int strSize = paramString.length();
        for (int j = 0; j < strSize; j++) {
            asciiValue += paramString.charAt(j);
        }
        byte[] hashBytes = md.digest((asciiValue + secret + "").getBytes());
        md.reset();
        String mac = "";
        String hexByte;
        for (int k = 0; k < hashBytes.length; k++) {
            hexByte = Integer.toHexString(hashBytes[k] < 0 ? hashBytes[k] + 256 : hashBytes[k]);
            mac += (hexByte.length() == 1) ? "0" + hexByte : hexByte;
        }
        return mac;
    }
}
