class BackupThread extends Thread {
    private static String getChecksum(File file, MessageDigest messageDigest) throws IOException {
        FileInputStream input = new FileInputStream(file);
        byte[] buffer = new byte[BUFFER_SIZE];
        int i = 0;
        while ((i = input.read(buffer, 0, BUFFER_SIZE)) > 0) {
            messageDigest.update(buffer, 0, i);
        }
        input.close();
        return formatHexBytes(messageDigest.digest());
    }
}
