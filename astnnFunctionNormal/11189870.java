class BackupThread extends Thread {
    private static String getDigestRepresentation(File fileToDigest) throws UnableToDigestFileException {
        MessageDigest messageDigest;
        FileInputStream inputStream = null;
        byte[] buffer = new byte[8129];
        int numberOfBytes;
        byte[] digestValue;
        BASE64Encoder encoder;
        String fileHash = new String();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            inputStream = new FileInputStream(fileToDigest.getAbsoluteFile());
            numberOfBytes = inputStream.read(buffer);
            while (numberOfBytes != -1) {
                messageDigest.update(buffer, 0, numberOfBytes);
                numberOfBytes = inputStream.read(buffer);
            }
            digestValue = messageDigest.digest();
            encoder = new BASE64Encoder();
            fileHash = encoder.encode(digestValue);
        } catch (IOException exception) {
            throw new UnableToDigestFileException(fileToDigest.getAbsolutePath(), exception);
        } catch (NoSuchAlgorithmException exception) {
            throw new UnableToDigestFileException(fileToDigest.getAbsolutePath(), exception);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return fileHash;
    }
}
