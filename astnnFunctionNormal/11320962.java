class BackupThread extends Thread {
    public String getCheckSum() {
        try {
            MessageDigest scriptDigest = getScriptDigest();
            return getHexPresentation(scriptDigest.digest());
        } catch (IOException e) {
            throw new DbMaintainException(e);
        }
    }
}
