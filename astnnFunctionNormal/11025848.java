class BackupThread extends Thread {
    public String digout() {
        byte[] digest = md.digest();
        if (digest != null) return StringUtils.hexEncode(digest); else return null;
    }
}
