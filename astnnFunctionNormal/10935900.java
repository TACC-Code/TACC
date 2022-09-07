class BackupThread extends Thread {
    public static String convertHashToString(MessageDigest messageDigest) {
        try {
            byte[] bHash = cloneMessageDigest(messageDigest).digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (int j = 0; j < bHash.length; j++) {
                stringBuffer.append(Integer.toHexString((bHash[j] >> 4) & 0x0f));
                stringBuffer.append(Integer.toHexString(bHash[j] & 0x0f));
            }
            return stringBuffer.toString();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
