class BackupThread extends Thread {
    private byte[] getDESKeyBytes(String sessionPassword) throws NoSuchAlgorithmException {
        int i, j, k;
        byte[] passBytes = getPasswordBytes(sessionPassword);
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        byte[] md5bytes = md5Digest.digest(passBytes);
        int[] convertedMD5Bytes = new int[7];
        for (i = 0; i < 7; i++) {
            convertedMD5Bytes[i] = (int) md5bytes[i] & 0xff;
        }
        String md5BytesString = "md5 bytes: ";
        for (i = 0; i < md5bytes.length; i++) {
            md5BytesString += Integer.toHexString(md5bytes[i]) + " ";
        }
        log.info(md5BytesString);
        byte[] desBytes = new byte[8];
        desBytes[0] = (byte) convertedMD5Bytes[0];
        desBytes[1] = (byte) (convertedMD5Bytes[0] << 7 | convertedMD5Bytes[1] >>> 1);
        desBytes[2] = (byte) (convertedMD5Bytes[1] << 6 | convertedMD5Bytes[2] >>> 2);
        desBytes[3] = (byte) (convertedMD5Bytes[2] << 5 | convertedMD5Bytes[3] >>> 3);
        desBytes[4] = (byte) (convertedMD5Bytes[3] << 4 | convertedMD5Bytes[4] >>> 4);
        desBytes[5] = (byte) (convertedMD5Bytes[4] << 3 | convertedMD5Bytes[5] >>> 5);
        desBytes[6] = (byte) (convertedMD5Bytes[5] << 2 | convertedMD5Bytes[6] >>> 6);
        desBytes[7] = (byte) (convertedMD5Bytes[6] << 1);
        String desKeyString = "des key (before pad): ";
        for (i = 0; i < 8; i++) {
            desKeyString += Integer.toHexString(desBytes[i]) + " ";
        }
        log.info(desKeyString);
        for (i = 0; i < 8; ++i) {
            k = desBytes[i] & 0xfe;
            j = k;
            j ^= j >>> 4;
            j ^= j >>> 2;
            j ^= j >>> 1;
            j = (j & 1) ^ 1;
            desBytes[i] = (byte) (k | j);
        }
        desKeyString = "des key: ";
        for (i = 0; i < 8; i++) {
            desKeyString += Integer.toHexString(desBytes[i]) + " ";
        }
        log.info(desKeyString);
        return desBytes;
    }
}
