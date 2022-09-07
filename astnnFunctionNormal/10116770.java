class BackupThread extends Thread {
    @SuppressWarnings("nls")
    private String getMD5CheckSum(java.io.InputStream is) {
        byte[] buffer = new byte[1024];
        String checkSum = new String();
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            int numRead;
            do {
                numRead = is.read(buffer);
                if (numRead > 0) {
                    digest.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            is.close();
            checkSum = new String(digest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkSum;
    }
}
