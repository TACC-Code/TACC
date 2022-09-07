class BackupThread extends Thread {
    private String getUniqueFileName(String lilypondCode, int size) {
        String digest = null;
        try {
            digest = HexUtil.getHexString(MessageDigest.getInstance("MD5").digest((lilypondCode + size).getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            LOG.warning(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOG.warning(e.getMessage());
        }
        if (digest == null) {
            digest = UUID.randomUUID().toString();
        }
        return digest;
    }
}
